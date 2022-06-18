package com.earthmovers.www.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.earthmovers.www.BuildConfig
import com.earthmovers.www.R
import com.earthmovers.www.databinding.FragmentMapsBinding
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.observeOnce
import com.earthmovers.www.utils.viewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class MapsFragment : BaseFragment(R.layout.fragment_maps), OnMapReadyCallback {

    private val binding by viewBinding(FragmentMapsBinding::bind)
    private val viewModel by activityViewModels<HomeViewModel>()
    private var googleMap: GoogleMap? = null
    private var isLocationPermissionOk = false
    private var currentLocation: Location? = null
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var permissionToRequest = mutableListOf<String>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context!!)

        viewModel.selectedPost.observeOnce(viewLifecycleOwner) {
            try {
                val address: List<Address> =
                    Geocoder(requireContext()).getFromLocationName(it.location, 5)
                val location = address[0]
                val lat = location.latitude
                val long = location.longitude
                val locationOfProject = LatLng(lat, long)

                viewModel.projectLocationReady(locationOfProject)

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Couldn't find address", Toast.LENGTH_SHORT).show()
            }
        }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isLocationPermissionOk =
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                            && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

                if (isLocationPermissionOk)
                    setUpMap()
                else
                    Toast.makeText(
                        requireContext(),
                        "Location permission denied",
                        Toast.LENGTH_LONG
                    )
                        .show()

            }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isLocationPermissionOk = false
            return
        }
        googleMap?.isMyLocationEnabled = true
        googleMap?.uiSettings?.isTiltGesturesEnabled = true
        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
        googleMap?.uiSettings?.isCompassEnabled = false

        getCurrentLocation()
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap!!.uiSettings.isZoomControlsEnabled = true

        when {
            ActivityCompat.checkSelfPermission(
                (activity as AppCompatActivity),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                isLocationPermissionOk = true
                setUpMap()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                (activity as AppCompatActivity),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                AlertDialog.Builder(context!!)
                    .setTitle("Location Permission")
                    .setMessage("Near me required location permission to access your location")
                    .setPositiveButton("Ok") { _, _ ->
                        requestLocation()
                    }.create().show()
            }

            else -> {
                requestLocation()
            }
        }
    }

    private fun getCurrentLocation() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity as AppCompatActivity)

        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isLocationPermissionOk = false
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                currentLocation = it

                viewModel.projectLocation.observeOnce(viewLifecycleOwner) { projectLocation ->
                    googleMap!!.apply {
                        addMarker(
                            MarkerOptions().position(projectLocation).title("Project Location")
                        )
                        addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    currentLocation!!.latitude,
                                    currentLocation!!.longitude
                                )
                            ).title("Current Location")
                        )
                        animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    currentLocation!!.latitude,
                                    currentLocation!!.longitude
                                ), 10f
                            )
                        )
                    }
                    getDirection(
                        "driving",
                        projectLocation,
                        LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                    )
                    val result = FloatArray(1)
                    Location.distanceBetween(
                        currentLocation!!.latitude,
                        currentLocation!!.longitude,
                        projectLocation.latitude,
                        projectLocation.longitude,
                        result
                    )
                    binding.distance.text = "${result[0] / 1000} KM"
                }
            } else {
                Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestLocation() {
        permissionToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissionToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        permissionLauncher.launch(permissionToRequest.toTypedArray())
    }

    private fun getDirection(mode: String, start: LatLng, end: LatLng) {
        val path: MutableList<List<LatLng>> = ArrayList()
        val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + start.latitude + "," + start.longitude +
                "&destination=" + end.latitude + "," + end.longitude +
                "&sensor= false" +
                "&mode=" + mode +
                "&key=" + BuildConfig.MAPS_API_KEY

        val directionsRequest = object :
            StringRequest(Method.GET, urlDirections, Response.Listener { response ->
                val jsonResponse = JSONObject(response)
                try {
                    // Get routes
                    val routes = jsonResponse.getJSONArray("routes")
                    val legs = routes.getJSONObject(0).getJSONArray("legs")
                    val steps = legs.getJSONObject(0).getJSONArray("steps")
                    for (i in 0 until steps.length()) {
                        val points =
                            steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                        path.add(PolyUtil.decode(points))
                    }
                    for (i in 0 until path.size) {
                        this.googleMap!!.addPolyline(
                            PolylineOptions().addAll(path[i]).color(Color.GREEN)
                        )
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Location not in driving range",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, Response.ErrorListener { error ->
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }) {}
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(directionsRequest)
    }
}