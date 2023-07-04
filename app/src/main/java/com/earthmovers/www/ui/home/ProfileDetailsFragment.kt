package com.earthmovers.www.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.earthmovers.www.R
import com.earthmovers.www.data.remote.GetUserBody
import com.earthmovers.www.databinding.FragmentProfileBinding
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.setGone
import com.earthmovers.www.utils.setVisible
import com.earthmovers.www.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDetailsFragment : BaseFragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchMode.setGone()
        binding.uploadButton.setGone()
        binding.accepterOffers.setGone()
        binding.myProfilePlaceholder.text = getString(R.string.profile)

        viewModel.selectedPost.observe(viewLifecycleOwner) {
            viewModel.getUserWithId(GetUserBody(it.owner))
        }

        viewModel.onlineUserInfo.observe(viewLifecycleOwner){
            binding.fullName.text = it.name
            binding.phoneNumber.text = it.phone
            if (it.src != null) {
                Glide.with(this)
                    .load(it.src)
                    .centerCrop()
                    .placeholder(R.drawable.ic_person)
                    .dontAnimate()
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
}