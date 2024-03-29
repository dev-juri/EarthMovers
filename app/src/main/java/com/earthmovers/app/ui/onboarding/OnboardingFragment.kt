package com.earthmovers.app.ui.onboarding

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.earthmovers.app.R
import com.earthmovers.app.databinding.FragmentOnboardingBinding
import com.earthmovers.app.utils.BaseFragment
import com.earthmovers.app.utils.setGone
import com.earthmovers.app.utils.setVisible
import com.earthmovers.app.utils.viewBinding

class OnboardingFragment : BaseFragment(R.layout.fragment_onboarding) {

    private val binding by viewBinding(FragmentOnboardingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        val timer = object : CountDownTimer(8000, 1000) {
            override fun onTick(p0: Long) {
                when {
                    p0 > 6000L -> {
                        binding.introImg.setImageResource(R.drawable.splash_screen_image)
                        binding.introText.text = getString(R.string.get_your_dump_clean_in_a_click)
                        binding.one.setCardBackgroundColor(Color.WHITE)
                        binding.two.setCardBackgroundColor(
                            resources.getColorStateList(
                                R.color.gray,
                                null
                            )
                        )
                        binding.three.setCardBackgroundColor(
                            resources.getColorStateList(
                                R.color.gray,
                                null
                            )
                        )
                    }
                    p0 > 3000 -> {
                        binding.introImg.setImageResource(R.drawable.volvo)
                        binding.introText.text = getString(R.string.intro_text_two)
                        binding.one.setCardBackgroundColor(
                            resources.getColorStateList(
                                R.color.gray,
                                null
                            )
                        )
                        binding.two.setCardBackgroundColor(Color.WHITE)
                        binding.three.setCardBackgroundColor(
                            resources.getColorStateList(
                                R.color.gray,
                                null
                            )
                        )
                    }
                    else -> {
                        binding.introImg.setImageResource(R.drawable.splash_screen_two)
                        binding.introText.text = getString(R.string.intro_text_three)
                        binding.one.setCardBackgroundColor(
                            resources.getColorStateList(
                                R.color.gray,
                                null
                            )
                        )
                        binding.two.setCardBackgroundColor(
                            resources.getColorStateList(
                                R.color.gray,
                                null
                            )
                        )
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
            onboardingSuccessful()
            findNavController().navigate(R.id.action_onboardingFragment_to_signupFragment)
        }

        binding.getStarted.setOnClickListener {
            onboardingSuccessful()
            findNavController().navigate(R.id.action_onboardingFragment_to_signupFragment)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        (activity as AppCompatActivity).window.clearFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun onboardingSuccessful() {
        val sharedPref = (activity as AppCompatActivity).getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isUserFirstTime", false)
            apply()
        }
    }
}