package com.earthmovers.app.utils

import androidx.fragment.app.Fragment

interface BottomNavFragment {
    var showNavBar: Boolean
}

open class BottomNavTopLevelFragment(f: Int) : Fragment(f), BottomNavFragment {
    override var showNavBar: Boolean = true
}

open class BaseFragment(f: Int) : Fragment(f), BottomNavFragment {
    override var showNavBar: Boolean = false
}