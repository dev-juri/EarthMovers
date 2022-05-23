package com.earthmovers.www.ui.onboarding.signup

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.earthmovers.www.R
import com.earthmovers.www.databinding.FragmentSignupBinding
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.viewBinding

class SignupFragment: BaseFragment(R.layout.fragment_signup) {

    private val binding by viewBinding(FragmentSignupBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }
}