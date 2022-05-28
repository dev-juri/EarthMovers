package com.earthmovers.www.ui.post_job

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.earthmovers.www.R
import com.earthmovers.www.databinding.FragmentPostJobBinding
import com.earthmovers.www.utils.BottomNavTopLevelFragment
import com.earthmovers.www.utils.viewBinding
import java.io.File

class PostJobFragment: BottomNavTopLevelFragment(R.layout.fragment_post_job) {

    private val binding by viewBinding(FragmentPostJobBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.data
                binding.selectedImagePreview.setImageURI(data)
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
    }
}