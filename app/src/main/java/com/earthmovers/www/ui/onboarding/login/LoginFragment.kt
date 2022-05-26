package com.earthmovers.www.ui.onboarding.login

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.earthmovers.www.R
import com.earthmovers.www.databinding.FragmentLoginBinding
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.viewBinding

class LoginFragment: BaseFragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    var areFieldsValidated = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validateFields()

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailBox.editText?.text.toString()
            val password = binding.passwordBox.editText?.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "No field should be left blank",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if(areFieldsValidated){
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please, check your input and try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun validateFields() {
        binding.emailBox.editText?.addTextChangedListener {
            when {
                !Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches() -> {
                    binding.emailBox.apply {
                        isErrorEnabled = true
                        error = "Invalid Email Address"
                    }
                    areFieldsValidated = false
                }
                it.isNullOrEmpty() -> {
                    binding.emailBox.apply {
                        error = "Required Field"
                        isErrorEnabled = true
                    }
                    areFieldsValidated = false
                }
                else -> {
                    binding.emailBox.isErrorEnabled = false
                    areFieldsValidated = true
                }
            }
        }
        binding.passwordBox.editText?.addTextChangedListener {
            when {
                it.isNullOrEmpty() -> {
                    binding.passwordBox.apply {
                        error = "Required Field"
                        isErrorEnabled = true
                    }
                    areFieldsValidated = false
                }
                else -> {
                    binding.passwordBox.isErrorEnabled = false
                    areFieldsValidated = true
                }
            }
        }
    }
}