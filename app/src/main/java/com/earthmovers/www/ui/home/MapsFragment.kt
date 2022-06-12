package com.earthmovers.www.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.earthmovers.www.R
import com.earthmovers.www.utils.BaseFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : BaseFragment(R.layout.fragment_maps) {

    private val viewModel by activityViewModels<HomeViewModel>()
    private lateinit var googleMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context!!)
        
        val coder = Geocoder(requireContext())
        var address: List<Address>

        viewModel.selectedPost.observe(viewLifecycleOwner) {
            try {
                address = coder.getFromLocationName(it.location, 5)
                val location = address[0]
                val lat = location.latitude
                val long = location.longitude
                val locationOfProject = LatLng(lat, long)

                val callback = OnMapReadyCallback { p0 ->

                    googleMap = p0
                    googleMap.uiSettings.isZoomControlsEnabled = true

                    setUpMap()

                    googleMap.addMarker(
                        MarkerOptions().position(locationOfProject).title("Project Location")
                    )
                    //googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationOfProject))
                }
                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync(callback)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Couldn't find address", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (activity as AppCompatActivity),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE
            )
            return
        }

        googleMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)

                googleMap.addMarker(
                    MarkerOptions().position(currentLatLong).title("Current Location")
                )
                Toast.makeText(requireContext(), location.toString(), Toast.LENGTH_SHORT).show()

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
            } else {
                Toast.makeText(requireContext(), "Location is null", Toast.LENGTH_SHORT).show()

            }
        }
    }


    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }
}