package com.earthmovers.www.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import com.earthmovers.www.R
import com.earthmovers.www.utils.BaseFragment

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
        }, 3000)
    }
}