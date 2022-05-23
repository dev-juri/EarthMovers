package com.earthmovers.www.ui.onboarding

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.earthmovers.www.R
import com.earthmovers.www.databinding.FragmentOnboardingBinding
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.setGone
import com.earthmovers.www.utils.setVisible
import com.earthmovers.www.utils.viewBinding

class OnboardingFragment : BaseFragment(R.layout.fragment_onboarding) {

    private val binding by viewBinding(FragmentOnboardingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timer = object: CountDownTimer(9000, 1000) {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onTick(p0: Long) {
                when {
                    p0 > 6000L -> {
                        binding.introImg.setImageResource(R.drawable.splash_screen_image)
                        binding.introText.text = getString(R.string.get_your_dump_clean_in_a_click)
                        binding.one.setCardBackgroundColor(Color.WHITE)
                        binding.two.setCardBackgroundColor(resources.getColorStateList(R.color.gray, null))
                        binding.three.setCardBackgroundColor(resources.getColorStateList(R.color.gray, null))
                    }
                    p0 > 3000 -> {
                        binding.introImg.setImageResource(R.drawable.volvo)
                        binding.introText.text = getString(R.string.intro_text_two)
                        binding.one.setCardBackgroundColor(resources.getColorStateList(R.color.gray, null))
                        binding.two.setCardBackgroundColor(Color.WHITE)
                        binding.three.setCardBackgroundColor(resources.getColorStateList(R.color.gray, null))
                    }
                    else -> {
                        binding.introImg.setImageResource(R.drawable.splash_screen_two)
                        binding.introText.text = getString(R.string.intro_text_three)
                        binding.one.setCardBackgroundColor(resources.getColorStateList(R.color.gray, null))
                        binding.two.setCardBackgroundColor(resources.getColorStateList(R.color.gray, null))
                        binding.three.setCardBackgroundColor(Color.WHITE)
                    }
                }
            }

            override fun onFinish() {
                binding.getStarted.setVisible()
                binding.indicator.setGone()
                binding.skip.setGone()
            }

        }

        timer.start()

        binding.skip.setOnClickListener {
            timer.cancel()
            findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToSignupFragment())
        }

        binding.getStarted.setOnClickListener {
            findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToSignupFragment())

        }
    }
}