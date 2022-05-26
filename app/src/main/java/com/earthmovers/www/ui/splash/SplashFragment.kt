package com.earthmovers.www.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.earthmovers.www.R
import com.earthmovers.www.utils.BaseFragment

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref =
            (activity as AppCompatActivity).getPreferences(Context.MODE_PRIVATE) ?: return
        val isUserFirstTime = sharedPref.getBoolean("isUserFirstTime", true)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isUserFirstTime) {
                findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
            } else {
                //findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }, 2000)
    }
}