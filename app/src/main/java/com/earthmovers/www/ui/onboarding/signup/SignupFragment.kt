package com.earthmovers.www.ui.onboarding.signup

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.earthmovers.www.R
import com.earthmovers.www.databinding.FragmentSignupBinding
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.viewBinding

class SignupFragment : BaseFragment(R.layout.fragment_signup) {

    private val binding by viewBinding(FragmentSignupBinding::bind)
    var areFieldsValidated = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validateFields()

        binding.backButton.setOnClickListener {

        }
        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        binding.signupButton.setOnClickListener {
            val firstName = binding.fullNameBox.editText?.text.toString()
            val email = binding.emailBox.editText?.text.toString()
            val phoneNumber = binding.numberBox.editText?.text.toString()
            val password = binding.passwordBox.editText?.text.toString()
            val confirmPassword = binding.confirmPasswordBox.editText?.text.toString()

            if (firstName.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty() || phoneNumber.isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "No field should be left blank",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (areFieldsValidated) {
                    Toast.makeText(
                        requireContext(),
                        "Validated",
                        Toast.LENGTH_SHORT
                    ).show()
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
        binding.fullNameBox.editText?.addTextChangedListener {
            when {
                it.isNullOrEmpty() -> {
                    binding.fullNameBox.apply {
                        error = "Required Field"
                        isErrorEnabled = true
                    }
                    areFieldsValidated = false
                }
                else -> {
                    binding.fullNameBox.isErrorEnabled = false
                    areFieldsValidated = true
                }
            }
        }
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
        binding.numberBox.editText?.addTextChangedListener {
            when {
                it.isNullOrEmpty() -> {
                    binding.numberBox.apply {
                        isErrorEnabled = true
                        error = "Required Field"
                    }
                    areFieldsValidated = false
                }
                else -> {
                    binding.numberBox.isErrorEnabled = false
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
                it.length < 8 -> {
                    binding.passwordBox.apply {
                        error = "Password not long enough"
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
        binding.confirmPasswordBox.editText?.addTextChangedListener {
            when {
                it.isNullOrEmpty() -> {
                    binding.confirmPasswordBox.apply {
                        error = "Required Field"
                        isErrorEnabled = true
                    }
                    areFieldsValidated = false
                }
                it.toString() != binding.passwordBox.editText?.text.toString() -> {
                    binding.confirmPasswordBox.apply {
                        error = "The passwords do not match"
                        isErrorEnabled = true
                    }
                    areFieldsValidated = false
                }
                else -> {
                    binding.confirmPasswordBox.isErrorEnabled = false
                    areFieldsValidated = true
                }
            }
        }
    }
}

