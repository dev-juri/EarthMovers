package com.earthmovers.app.ui.onboarding.forgot_password

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.earthmovers.app.R
import com.earthmovers.app.data.State
import com.earthmovers.app.data.remote.ChangePasswordBody
import com.earthmovers.app.databinding.FragmentResetPasswordBinding
import com.earthmovers.app.ui.ProgressDialog
import com.earthmovers.app.utils.BaseFragment
import com.earthmovers.app.utils.isOnline
import com.earthmovers.app.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment: BaseFragment(R.layout.fragment_reset_password) {

    private val binding by viewBinding(FragmentResetPasswordBinding::bind)
    private val viewModel: ForgotPasswordViewModel by activityViewModels()

    private val progressDialog = ProgressDialog()
    private lateinit var userID : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDataState()

        viewModel.userID.observe(viewLifecycleOwner) {
            userID = it
        }

        binding.resetPassword.setOnClickListener {
            val newPassword = binding.newPasswordBox.editText?.text?.trim().toString()
            val confirmNewPassword = binding.confirmNewPasswordBox.editText?.text?.trim().toString()

            if ((newPassword == confirmNewPassword) && (newPassword.length >= 8)) {
                if (isOnline(requireContext())) {
                    viewModel.resetPassword(ChangePasswordBody(userID = userID, password = newPassword))
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Passwords should be the same with at least length of 8",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeDataState() {
        viewModel.dataState.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    State.SUCCESS -> {
                        progressDialog.dismiss()

                        Toast.makeText(
                            requireContext(),
                            "Password reset successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        this.findNavController()
                            .navigate(R.id.action_resetPasswordFragment_to_loginFragment)
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