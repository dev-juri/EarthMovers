package com.earthmovers.www.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.earthmovers.www.R
import com.earthmovers.www.databinding.FragmentOnboardingBinding
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.viewBinding

class OnboardingFragment : BaseFragment(R.layout.fragment_onboarding) {

    private val binding by viewBinding(FragmentOnboardingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.skip.setOnClickListener {
            findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToSignupFragment())
        }

        binding.getStarted.setOnClickListener {
            findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToSignupFragment())

        }
    }
}