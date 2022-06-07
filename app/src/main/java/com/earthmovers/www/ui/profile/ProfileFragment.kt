package com.earthmovers.www.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.earthmovers.www.R
import com.earthmovers.www.data.State
import com.earthmovers.www.data.domain.User
import com.earthmovers.www.databinding.FragmentProfileBinding
import com.earthmovers.www.ui.viewmodels.ProfileViewModel
import com.earthmovers.www.utils.BottomNavTopLevelFragment
import com.earthmovers.www.utils.setGone
import com.earthmovers.www.utils.setVisible
import com.earthmovers.www.utils.viewBinding

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
                binding.fullName.text = it.name
                binding.phoneNumber.text = it.phone
                if (it.src != null) {
                    Glide.with(this)
                        .load(it.src)
                        .centerCrop()
                        .placeholder(R.drawable.ic_image_placeholder)
                        .into(binding.profilePicture)
                }
                if (it.isVendor == true) {
                    binding.switchMode.setGone()
                } else {
                    binding.switchMode.setVisible()
                }
            }
        }
        viewModel.getUserWithId(user)

        viewModel.imageURI.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.updateUser(requireContext(), user)
            }
        }

        viewModel.dataState.observe(viewLifecycleOwner) {
            if (it == State.ERROR) {
                Toast.makeText(
                    requireContext(),
                    "Couldn't upload image, Please try again",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.clearImageData()
                binding.profilePicture.setImageResource(R.drawable.ic_person)
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
}