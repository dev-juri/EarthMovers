package com.earthmovers.app.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.earthmovers.app.R
import com.earthmovers.app.data.State
import com.earthmovers.app.data.domain.RecentProject
import com.earthmovers.app.data.domain.User
import com.earthmovers.app.data.remote.GetUserBody
import com.earthmovers.app.databinding.FragmentProjectDetailsBinding
import com.earthmovers.app.ui.ProgressDialog
import com.earthmovers.app.utils.BaseFragment
import com.earthmovers.app.utils.isOnline
import com.earthmovers.app.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectDetailsFragment : BaseFragment(R.layout.fragment_project_details) {

    private val binding by viewBinding(FragmentProjectDetailsBinding::bind)
    private val viewModel by activityViewModels<HomeViewModel>()
    private val progressDialog = ProgressDialog()

    private lateinit var userDetails: User
    private lateinit var selectedPost: RecentProject

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.resetState()
        observeDataState()

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.viewProfile.setOnClickListener {
            findNavController().navigate(R.id.action_projectDetailsFragment_to_profileDetailsFragment)
        }

        viewModel.selectedPost.observe(viewLifecycleOwner) {
            selectedPost = it
            binding.nameOfPoster.text = it.name
            binding.jobDetails.text = it.projectHighlight
            binding.projectLocation.text = it.location
            binding.phoneNumber.text = it.phone
            binding.locationOfPoster.text = it.location

            viewModel.getUserWithId(GetUserBody(it.owner))

            Glide.with(this)
                .load(it.image)
                .centerCrop()
                .placeholder(R.drawable.ic_image_placeholder)
                .dontAnimate()
                .into(binding.projectImage)
        }

        viewModel.onlineUserInfo.observe(viewLifecycleOwner) {
            if (it != null) {
                Glide.with(this)
                    .load(it.src)
                    .centerCrop()
                    .placeholder(R.drawable.ic_person)
                    .dontAnimate()
                    .into(binding.profilePicture)
            }
        }

        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                userDetails = it
            }
        }

        binding.checkMap.setOnClickListener {
            findNavController().navigate(R.id.action_projectDetailsFragment_to_mapsFragment)
        }

        binding.acceptOffer.setOnClickListener {
            if (userDetails.id == selectedPost.owner) {
                Toast.makeText(
                    requireContext(),
                    "You cannot accepted an offer you posted",
                    Toast.LENGTH_LONG
                ).show()
            } else if (userDetails.src == null && userDetails.isVendor == false) {
                Toast.makeText(
                    requireContext(),
                    "You need to upload a picture and switch to a Vendor profile to accept an offer",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if (isOnline(requireContext())) {
                    if (userDetails.src != null) {
                        viewModel.acceptOffer(userDetails)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Kindly update your profile and upload a profile picture.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "No Internet Connection.",
                        Toast.LENGTH_LONG
                    ).show()
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
                        Toast.makeText(
                            requireContext(),
                            "Offer Accepted Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetState()
    }
}