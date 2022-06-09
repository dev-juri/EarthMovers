package com.earthmovers.www.ui.home

import android.os.Bundle
import android.view.View
import com.earthmovers.www.R
import com.earthmovers.www.databinding.FragmentMapBinding
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment: BaseFragment(R.layout.fragment_map) {
    private val binding by viewBinding(FragmentMapBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}