package com.earthmovers.www

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.earthmovers.www.databinding.ActivityMainBinding
import com.earthmovers.www.utils.BottomNavFragment
import com.earthmovers.www.utils.setGone
import com.earthmovers.www.utils.setVisible


class MainActivity : AppCompatActivity() {
    private inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
        crossinline bindingInflater: (LayoutInflater) -> T
    ) =
        lazy(LazyThreadSafetyMode.NONE) {
            bindingInflater.invoke(layoutInflater)
        }

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            super.onFragmentResumed(fm, f)

            (f as? BottomNavFragment)?.apply {
                if (f.showNavBar) {
                    binding.bottomNav.setVisible()
                } else {
                    binding.bottomNav.setGone()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestWindowFeature(1)
        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            statusBarColor = Color.TRANSPARENT
        }
        setContentView(binding.root)

        (this as FragmentActivity).supportFragmentManager.registerFragmentLifecycleCallbacks(
            fragmentLifecycleCallbacks,
            true
        )

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentNavHost) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
    }


    override fun onDestroy() {
        super.onDestroy()
        (this as FragmentActivity).supportFragmentManager.unregisterFragmentLifecycleCallbacks(
            fragmentLifecycleCallbacks
        )
    }

}
