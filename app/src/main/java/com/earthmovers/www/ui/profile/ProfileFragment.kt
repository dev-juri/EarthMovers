package com.earthmovers.www.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.earthmovers.www.R
import com.earthmovers.www.databinding.FragmentProfileBinding
import com.earthmovers.www.ui.viewmodels.ProfileViewModel
import com.earthmovers.www.utils.BottomNavTopLevelFragment
import com.earthmovers.www.utils.viewBinding

class ProfileFragment : BottomNavTopLevelFragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchMode.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_contractorModeFormFragment)
        }

        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.fullName.text = it.name
                binding.phoneNumber.text = it.phone
            }
        }

        val imageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data?.data

                    if (data != null) {
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