package com.earthmovers.www.ui.profile

import android.os.Bundle
import android.view.View
import com.earthmovers.www.R
import com.earthmovers.www.databinding.FragmentContractorModeFormBinding
import com.earthmovers.www.utils.BaseFragment
import com.earthmovers.www.utils.viewBinding

class ContractorModeFormFragment: BaseFragment(R.layout.fragment_contractor_mode_form) {

    private val binding by viewBinding(FragmentContractorModeFormBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}