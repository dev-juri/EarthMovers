package com.earthmovers.app.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.earthmovers.app.R
import com.earthmovers.app.data.State
import com.earthmovers.app.databinding.FragmentContractorModeFormBinding
import com.earthmovers.app.ui.ProgressDialog
import com.earthmovers.app.ui.viewmodels.ProfileViewModel
import com.earthmovers.app.utils.*

class ContractorModeFormFragment : BaseFragment(R.layout.fragment_contractor_mode_form) {

    private val binding by viewBinding(FragmentContractorModeFormBinding::bind)
    private val viewModel: ProfileViewModel by activityViewModels()
    private val progressDialog = ProgressDialog()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.resetState()
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

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.uploadImage.setOnClickListener {
            val intent = Intent()
            intent.apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            imageResultLauncher.launch(intent)
        }

        binding.submitButton.setOnClickListener {
            val desc = binding.descriptionBox.editText?.text.toString().trim()
            val phoneNum = binding.numberBox.editText?.text.toString().trim()
            val truckNum = binding.truckPlateNumberBox.editText?.text.toString().trim()

            if (noFieldBlank || desc.isNotEmpty() || phoneNum.isNotEmpty() || truckNum.isNotEmpty()) {
                if (isOnline(requireContext())) {
                    viewModel.user.observe(viewLifecycleOwner) {
                        if (it != null) {
                            viewModel.createVendor(requireContext(), desc, phoneNum, truckNum, it)
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
                            "Successfully Updated Contractor Details",
                            Toast.LENGTH_LONG
                        ).show()
                        viewModel.resetState()
                        findNavController().navigateUp()
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
        binding.descriptionBox.editText?.text?.clear()
        binding.numberBox.editText?.text?.clear()
        binding.truckPlateNumberBox.editText?.text?.clear()
        binding.selectedImagePreview.apply {
            setImageURI(null)
            setGone()
        }
    }
}