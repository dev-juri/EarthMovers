package com.earthmovers.www.ui.post_job

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.earthmovers.www.R
import com.earthmovers.www.data.State
import com.earthmovers.www.data.domain.User
import com.earthmovers.www.databinding.FragmentPostJobBinding
import com.earthmovers.www.ui.ProgressDialog
import com.earthmovers.www.ui.viewmodels.PostsViewModel
import com.earthmovers.www.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostJobFragment : BottomNavTopLevelFragment(R.layout.fragment_post_job) {

    private val binding by viewBinding(FragmentPostJobBinding::bind)
    private val viewModel by viewModels<PostsViewModel>()
    private val progressDialog = ProgressDialog()

    private lateinit var userDetails: User

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDataState()
        var noFieldBlank = false

        val imageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data?.data

                    noFieldBlank = if (data != null) {
                        binding.selectedImagePreview.setImageURI(data)
                        binding.selectedImagePreview.setVisible()

                        viewModel.setImageData(data)
                        true
                    } else {
                        false
                    }
                }
            }


        binding.uploadImage.setOnClickListener {
            val intent = Intent()
            intent.apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            imageResultLauncher.launch(intent)
        }

        binding.postButton.setOnClickListener {
            val details = binding.detailsBox.editText?.text.toString().trim()
            val location = binding.siteLocation.editText?.text.toString().trim()
            val phoneNumber = binding.phoneNumber.editText?.text.toString().trim()
            if (noFieldBlank && details.isNotEmpty() && location.isNotEmpty() && phoneNumber.isNotEmpty()) {
                if (isOnline(requireContext())) {
                    viewModel.user.observe(viewLifecycleOwner) {
                        if (it != null) {
                            viewModel.makePost(requireContext(), details, location, phoneNumber, it)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Select an image to continue",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "No field should be left blank",
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

                        resetTextFields()
                        Toast.makeText(
                            requireContext(),
                            "Post Successfully uploaded",
                            Toast.LENGTH_LONG
                        ).show()
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

    private fun resetTextFields() {
        binding.detailsBox.editText?.text?.clear()
        binding.siteLocation.editText?.text?.clear()
        binding.phoneNumber.editText?.text?.clear()
        binding.selectedImagePreview.apply {
            setImageURI(null)
            setGone()
        }
    }
}