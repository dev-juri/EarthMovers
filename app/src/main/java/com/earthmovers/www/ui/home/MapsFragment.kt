package com.earthmovers.www.ui.home

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.earthmovers.www.R
import com.earthmovers.www.databinding.FragmentMapsBinding
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : BaseFragment(R.layout.fragment_maps) {

    private val binding by viewBinding(FragmentMapsBinding::bind)
    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val coder = Geocoder(requireContext())
        var address: List<Address>

        viewModel.selectedPost.observe(viewLifecycleOwner) {
            try {
                address = coder.getFromLocationName(it.location, 5)
                val location = address[0]
                val lat = location.latitude
                val long = location.longitude
                val locationOfProject = LatLng(lat, long)

                val callback = OnMapReadyCallback { googleMap ->

                    googleMap.addMarker(
                        MarkerOptions().position(locationOfProject).title("Project Location")
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationOfProject))
                }
                val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync(callback)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Couldn't find address", Toast.LENGTH_SHORT).show()
            }
        }

    }
}