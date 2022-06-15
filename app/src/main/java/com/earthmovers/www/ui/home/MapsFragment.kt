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
import androidx.lifecycle.lifecycleScope
import com.earthmovers.www.BuildConfig
import com.earthmovers.www.R
import com.earthmovers.www.data.NetworkResult
import com.earthmovers.www.data.remote.DirectionLegModel
import com.earthmovers.www.data.remote.DirectionResponseModel
import com.earthmovers.www.data.remote.DirectionRouteModel
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.observeOnce
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class MapsFragment : BaseFragment(R.layout.fragment_maps), OnMapReadyCallback {

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
                    }
                    getDirection(
                        "driving",
                        projectLocation,
                        LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                    )

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

        if (isLocationPermissionOk) {
            val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "origin=" + start.latitude + "," + start.longitude +
                    "&destination=" + end.latitude + "," + end.longitude +
                    "&sensor= false" +
                    "&mode=" + mode +
                    "&key=" + BuildConfig.MAPS_API_KEY

            lifecycleScope.launchWhenStarted {
                viewModel.getDirection(url).collect {
                    when (it) {
                        is NetworkResult.Loading -> {}

                        is NetworkResult.Success -> {
                            val directionResponseModel: DirectionResponseModel =
                                it.data as DirectionResponseModel
                            val routeModel: DirectionRouteModel =
                                directionResponseModel.directionRouteModels!![0]

                            val legModel: DirectionLegModel = routeModel.legs?.get(0)!!

                            val stepList: MutableList<LatLng> = ArrayList()

                            val options = PolylineOptions().apply {
                                width(25f)
                                color(Color.BLUE)
                                geodesic(true)
                                clickable(true)
                                visible(true)
                            }

                            val pattern: List<PatternItem>

                            if (mode == "walking") {
                                pattern = listOf(
                                    Dot(), Gap(10f)
                                )

                                options.jointType(JointType.ROUND)
                            } else {

                                pattern = listOf(
                                    Dash(30f)
                                )

                            }

                            options.pattern(pattern)
                            for (stepModel in legModel.steps!!) {
                                val decodedList = decode(stepModel.polyline?.points!!)
                                for (latLng in decodedList) {
                                    stepList.add(
                                        LatLng(
                                            latLng.latitude,
                                            latLng.longitude
                                        )
                                    )
                                }
                            }

                            options.addAll(stepList)
                            googleMap?.addPolyline(options)
                            val startLocation = LatLng(
                                legModel.startLocation?.lat!!,
                                legModel.startLocation.lng!!
                            )

                            val endLocation = LatLng(
                                legModel.endLocation?.lat!!,
                                legModel.endLocation.lng!!
                            )

                            googleMap?.addMarker(
                                MarkerOptions()
                                    .position(endLocation)
                                    .title("End Location")
                            )

                            googleMap?.addMarker(
                                MarkerOptions()
                                    .position(startLocation)
                                    .title("Start Location")
                            )

                            val builder = LatLngBounds.builder()
                            builder.include(endLocation).include(startLocation)
                            val latLngBounds = builder.build()


                            googleMap?.animateCamera(
                                CameraUpdateFactory.newLatLngBounds(
                                    latLngBounds, 0
                                )
                            )
                        }
                        is NetworkResult.Error -> {
                            Timber.tag("Femi").v(it.exception.toString())
                            Toast.makeText(
                                requireContext(), it.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun decode(points: String): List<LatLng> {
        val len = points.length
        val path: MutableList<LatLng> = ArrayList(len / 2)
        var index = 0
        var lat = 0
        var lng = 0
        while (index < len) {
            var result = 1
            var shift = 0
            var b: Int
            do {
                b = points[index++].code - 63 - 1
                result += b shl shift
                shift += 5
            } while (b >= 0x1f)
            lat += if (result and 1 != 0) (result shr 1).inv() else result shr 1
            result = 1
            shift = 0
            do {
                b = points[index++].code - 63 - 1
                result += b shl shift
                shift += 5
            } while (b >= 0x1f)
            lng += if (result and 1 != 0) (result shr 1).inv() else result shr 1
            path.add(LatLng(lat * 1e-5, lng * 1e-5))
        }
        return path
    }

}