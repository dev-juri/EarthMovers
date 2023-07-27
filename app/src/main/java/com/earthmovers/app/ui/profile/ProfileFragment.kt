package com.earthmovers.app.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.earthmovers.app.R
import com.earthmovers.app.data.State
import com.earthmovers.app.data.domain.User
import com.earthmovers.app.databinding.FragmentProfileBinding
import com.earthmovers.app.ui.viewmodels.ProfileViewModel
import com.earthmovers.app.utils.*

class ProfileFragment : BottomNavTopLevelFragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var user: User

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchMode.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_contractorModeFormFragment)
        }

        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                user = it
                viewModel.profileLoaded()
                binding.fullName.text = it.name
                binding.phoneNumber.text = it.phone
                if (it.src != null) {
                    Glide.with(this)
                        .load(it.src)
                        .centerCrop()
                        .placeholder(R.drawable.ic_person)
                        .into(binding.profilePicture)
                }
                if (it.isVendor == true) {
                    binding.description.text = it.description
                    binding.truckNum.text = it.truck_plate_number
                    Glide.with(this)
                        .load(it.truck_src)
                        .centerCrop()
                        .placeholder(R.drawable.ic_image_placeholder)
                        .into(binding.truckImage)
                    binding.vendorProfile.setVisible()
                    binding.normalProfile.setGone()
                } else {
                    binding.vendorProfile.setGone()
                    binding.normalProfile.setVisible()
                }
            }
        }

        viewModel.profileLoaded.observeOnce(viewLifecycleOwner) {
            if (it != null) {
                viewModel.getUserWithId(user)
            }
        }

        viewModel.imageURI.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.updateUser(requireContext(), user)
            }
        }

        viewModel.profilePicState.observe(viewLifecycleOwner) {
            when (it) {
                State.SUCCESS -> {
                    viewModel.resetState()
                    Toast.makeText(
                        requireContext(),
                        "Profile picture uploaded successfully.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                State.ERROR -> {
                    viewModel.clearImageData()
                    viewModel.resetState()
                    Toast.makeText(
                        requireContext(),
                        "Something went wrong, please try again later.",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.profilePicture.setImageResource(R.drawable.ic_person)
                }
                State.LOADING -> {
                    Toast.makeText(
                        requireContext(),
                        "Uploading...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }
        }

        viewModel.dataState.observe(viewLifecycleOwner) {
            when (it) {
                State.SUCCESS -> {
                    viewModel.resetState()
                }
                State.ERROR -> {
                    viewModel.clearImageData()
                    viewModel.resetState()
                    binding.profilePicture.setImageResource(R.drawable.ic_person)
                }
                else -> {
                }
            }
        }

        val imageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data?.data

                    if (data != null) {
                        viewModel.setImageData(data)
                        binding.profilePicture.setImageURI(data)
                    }
                }
            }

        binding.uploadButton.setOnClickListener {
            val intent = Intent()
            intent.apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            imageResultLauncher.launch(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.profileLoaded.observeOnce(viewLifecycleOwner) {
            if (it != null) {
                viewModel.getUserWithId(user)
            }
        }
    }
}