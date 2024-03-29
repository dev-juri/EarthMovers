package com.earthmovers.app.ui.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.earthmovers.app.R
import com.earthmovers.app.databinding.FragmentProfileBinding
import com.earthmovers.app.ui.viewmodels.NotificationsViewModel
import com.earthmovers.app.utils.BaseFragment
import com.earthmovers.app.utils.setGone
import com.earthmovers.app.utils.setVisible
import com.earthmovers.app.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationProfileDetailsFragment : BaseFragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel: NotificationsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchMode.setGone()
        binding.uploadButton.setGone()
        binding.accepterOffers.setGone()
        binding.myProfilePlaceholder.text = getString(R.string.profile)

        viewModel.onlineUserInfo.observe(viewLifecycleOwner){
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
}