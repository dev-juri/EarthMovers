package com.earthmovers.app.ui.onboarding.forgot_password

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.earthmovers.app.R
import com.earthmovers.app.data.State
import com.earthmovers.app.data.remote.ForgotPasswordBody
import com.earthmovers.app.databinding.FragmentForgotPasswordConfirmationBinding
import com.earthmovers.app.ui.ProgressDialog
import com.earthmovers.app.utils.BaseFragment
import com.earthmovers.app.utils.isOnline
import com.earthmovers.app.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordConfirmationFragment :
    BaseFragment(R.layout.fragment_forgot_password_confirmation) {

    private val binding by viewBinding(FragmentForgotPasswordConfirmationBinding::bind)
    private val viewModel: ForgotPasswordViewModel by activityViewModels()

    private val progressDialog = ProgressDialog()

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