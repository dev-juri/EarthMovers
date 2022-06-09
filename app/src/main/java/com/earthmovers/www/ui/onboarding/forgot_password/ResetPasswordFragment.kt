package com.earthmovers.www.ui.onboarding.forgot_password

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.earthmovers.www.R
import com.earthmovers.www.databinding.FragmentResetPasswordBinding
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment: BaseFragment(R.layout.fragment_reset_password) {

    private val binding by viewBinding(FragmentResetPasswordBinding::bind)
    private val viewModel: ForgotPasswordViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}