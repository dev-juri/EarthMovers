package com.earthmovers.www.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.earthmovers.www.R
import com.earthmovers.www.data.remote.GetUserBody
import com.earthmovers.www.databinding.FragmentProjectDetailsBinding
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectDetailsFragment : BaseFragment(R.layout.fragment_project_details) {

    private val binding by viewBinding(FragmentProjectDetailsBinding::bind)
    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.viewProfile.setOnClickListener {
            findNavController().navigate(R.id.action_projectDetailsFragment_to_profileDetailsFragment)
        }

        viewModel.selectedPost.observe(viewLifecycleOwner) {
            binding.nameOfPoster.text = it.name
            binding.jobDetails.text = it.projectHighlight
            binding.projectLocation.text = it.location
            binding.phoneNumber.text = it.phone

            viewModel.getUserWithId(GetUserBody(it.owner))

            Glide.with(this)
                .load(it.image)
                .centerCrop()
                .placeholder(R.drawable.ic_image_placeholder)
                .into(binding.projectImage)
        }

    }
}