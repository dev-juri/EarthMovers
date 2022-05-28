package com.earthmovers.www.ui.onboarding.login

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
import com.earthmovers.www.data.remote.LoginBody
import com.earthmovers.www.databinding.FragmentLoginBinding
import com.earthmovers.www.ui.ProgressDialog
import com.earthmovers.www.ui.viewmodels.OnboardingViewModel
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.isOnline
import com.earthmovers.www.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: BaseFragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    var areFieldsValidated = false
    private val viewModel: OnboardingViewModel by viewModels()
    private val progressDialog = ProgressDialog()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validateFields()
        observeDataState()

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailBox.editText?.text.toString().trim()
            val password = binding.passwordBox.editText?.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "No field should be left blank",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if(areFieldsValidated){
                    val loginBody = LoginBody(email, password)
                    if(isOnline(requireContext())){
                        viewModel.loginUser(loginBody)
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

    private fun observeDataState() {
        viewModel.dataState.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    State.SUCCESS -> {
                        progressDialog.dismiss()

                        this.findNavController()
                            .navigate(R.id.action_loginFragment_to_homeFragment)
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