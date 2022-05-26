package com.earthmovers.www.ui.onboarding

import androidx.lifecycle.ViewModel
import com.earthmovers.www.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor (private val repository: MainRepository): ViewModel(){

}