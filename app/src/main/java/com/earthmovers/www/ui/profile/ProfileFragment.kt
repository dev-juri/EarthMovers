package com.earthmovers.www.ui.profile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.earthmovers.www.R
import com.earthmovers.www.utils.BottomNavTopLevelFragment
import com.earthmovers.www.utils.viewBinding
import com.earthmovers.www.databinding.FragmentProfileBinding

class ProfileFragment: BottomNavTopLevelFragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchMode.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_contractorModeFormFragment)
        }

    }
}