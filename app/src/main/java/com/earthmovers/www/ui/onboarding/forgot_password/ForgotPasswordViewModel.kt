package com.earthmovers.www.ui.onboarding.forgot_password

import androidx.lifecycle.ViewModel
import com.earthmovers.www.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {
}