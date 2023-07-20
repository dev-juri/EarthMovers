package com.earthmovers.app.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.earthmovers.app.R
import com.earthmovers.app.ui.viewmodels.OnboardingViewModel
import com.earthmovers.app.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val viewModel: OnboardingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref =
            (activity as AppCompatActivity).getPreferences(Context.MODE_PRIVATE) ?: return
        val isUserFirstTime = sharedPref.getBoolean("isUserFirstTime", true)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isUserFirstTime) {
                findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
            } else {
                viewModel.user.observe(viewLifecycleOwner) {
                    if (it != null) {
                        findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                    } else {
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                }

            }
        }, 2000)
    }
}