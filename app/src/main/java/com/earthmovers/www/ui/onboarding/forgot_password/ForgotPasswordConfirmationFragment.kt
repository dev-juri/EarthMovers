package com.earthmovers.www.ui.onboarding.forgot_password

import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.earthmovers.www.R
import com.earthmovers.www.data.State
import com.earthmovers.www.data.remote.ForgotPasswordBody
import com.earthmovers.www.databinding.FragmentForgotPasswordConfirmationBinding
import com.earthmovers.www.ui.ProgressDialog
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.isOnline
import com.earthmovers.www.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordConfirmationFragment :
    BaseFragment(R.layout.fragment_forgot_password_confirmation) {

    private val binding by viewBinding(FragmentForgotPasswordConfirmationBinding::bind)
    private val viewModel: ForgotPasswordViewModel by activityViewModels()

    private val progressDialog = ProgressDialog()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDataState()

        binding.send.setOnClickListener {
            val email = binding.emailBox.editText?.text?.trim().toString()
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(
                    requireContext(),
                    "Please check input and try again",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (isOnline(requireContext())) {
                    viewModel.forgotPassword(ForgotPasswordBody(email))
                } else {
                    Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_SHORT)
                        .show()
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
                            .navigate(R.id.action_forgotPasswordConfirmationFragment_to_resetPasswordFragment)
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