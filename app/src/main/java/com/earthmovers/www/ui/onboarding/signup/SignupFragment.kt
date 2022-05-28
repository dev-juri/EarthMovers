package com.earthmovers.www.ui.onboarding.signup

import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.earthmovers.www.R
import com.earthmovers.www.data.State
import com.earthmovers.www.databinding.FragmentSignupBinding
import com.earthmovers.www.ui.ProgressDialog
import com.earthmovers.www.ui.viewmodels.OnboardingViewModel
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.isOnline
import com.earthmovers.www.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : BaseFragment(R.layout.fragment_signup) {

    private val binding by viewBinding(FragmentSignupBinding::bind)
    private val viewModel: OnboardingViewModel by viewModels()
    private val progressDialog = ProgressDialog()

    var areFieldsValidated = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validateFields()
        observeDataState()

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        binding.signupButton.setOnClickListener {
            val fullName = binding.fullNameBox.editText?.text.toString().trim()
            val email = binding.emailBox.editText?.text.toString().trim()
            val phoneNumber = binding.numberBox.editText?.text.toString().trim()
            val password = binding.passwordBox.editText?.text.toString().trim()
            val confirmPassword = binding.confirmPasswordBox.editText?.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty() || phoneNumber.isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "No field should be left blank",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (areFieldsValidated) {
                    if (isOnline(requireContext())) {
                        viewModel.signupUser(fullName, email, phoneNumber, password)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No Internet Connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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
                !Patterns.EMAIL_ADDRESS.matcher(it.toString().trim()).matches() -> {
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
                it.trim().length < 8 -> {
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
                it.trim().toString() != binding.passwordBox.editText?.text.toString() -> {
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

    private fun observeDataState() {
        viewModel.dataState.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    State.SUCCESS -> {
                        progressDialog.dismiss()

                        this.findNavController()
                            .navigate(R.id.action_signupFragment_to_homeFragment)
                        viewModel.resetState()
                    }
                    State.ERROR -> {
                        progressDialog.dismiss()
                        Toast.makeText(
                            requireContext(),
                            viewModel.errorMessage.value.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.resetState()
                    }
                    else -> {
                        progressDialog.show(childFragmentManager, ProgressDialog.TAG)
                    }
                }
            }
        }
    }
}

