package com.earthmovers.www.ui.home

import android.os.Bundle
import android.view.View
import com.earthmovers.www.R
import com.earthmovers.www.adapters.ImageDetails
import com.earthmovers.www.adapters.ImageSlideAdapter
import com.earthmovers.www.adapters.RecentProjectsAdapter
import com.earthmovers.www.data.RecentProject
import com.earthmovers.www.databinding.FragmentHomeBinding
import com.earthmovers.www.utils.BottomNavTopLevelFragment
import com.earthmovers.www.utils.viewBinding

class HomeFragment : BottomNavTopLevelFragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var viewPagerAdapter: ImageSlideAdapter
    private lateinit var imagesForSlider: List<ImageDetails>
    private lateinit var recentProjectRecyclerAdapter: RecentProjectsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imagesForSlider = listOf(
            ImageDetails(
                getString(R.string.earn_xtra),
                R.drawable.hauling_vehicle
            ),
            ImageDetails(
                getString(R.string.get_dump_trashed),
                R.drawable.image
            )
        )

        viewPagerAdapter = ImageSlideAdapter(imagesForSlider)
        binding.viewpager.adapter = viewPagerAdapter
        binding.indicator.setViewPager(binding.viewpager)

        val recentProjectList = listOf(
            RecentProject(
                "Anybody available to dispose dump and clear out Sewage",
                "New Orleans"
            ),
            RecentProject(
                "Need Sewage and dump bins cleared out; Anyone available?",
                "Philly"
            )
        )
        recentProjectRecyclerAdapter = RecentProjectsAdapter()
        recentProjectRecyclerAdapter.submitList(recentProjectList)
        binding.recentProjectRecycler.adapter = recentProjectRecyclerAdapter
    }
}