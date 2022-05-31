package com.earthmovers.www.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.earthmovers.www.R
import com.earthmovers.www.adapters.ImageDetails
import com.earthmovers.www.adapters.ImageSlideAdapter
import com.earthmovers.www.adapters.RecentProjectsAdapter
import com.earthmovers.www.data.State
import com.earthmovers.www.databinding.FragmentHomeBinding
import com.earthmovers.www.utils.BottomNavTopLevelFragment
import com.earthmovers.www.utils.setGone
import com.earthmovers.www.utils.setVisible
import com.earthmovers.www.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BottomNavTopLevelFragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var viewPagerAdapter: ImageSlideAdapter
    private lateinit var imagesForSlider: List<ImageDetails>
    private lateinit var recentProjectRecyclerAdapter: RecentProjectsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDataState()
        viewModel.getRemotePosts()
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

        recentProjectRecyclerAdapter = RecentProjectsAdapter()
        binding.recentProjectRecycler.adapter = recentProjectRecyclerAdapter

        viewModel.user.observe(viewLifecycleOwner) {
            binding.welcome.text = "Welcome \n${(it?.name)?.split(" ")?.get(0)}"
        }
        viewModel.posts.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.noPostEmptyState.setVisible()
                binding.recentProjectRecycler.setGone()
            } else {
                recentProjectRecyclerAdapter.submitList(it)
                binding.noPostEmptyState.setGone()
                binding.recentProjectRecycler.setVisible()
            }

        }

    }


    private fun observeDataState() {
        viewModel.dataState.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    State.SUCCESS -> {
                        binding.progressBar.setGone()

                        viewModel.resetState()
                    }
                    State.ERROR -> {
                        binding.progressBar.setGone()

                        viewModel.resetState()
                    }
                    else -> {
                        binding.progressBar.setVisible()
                    }
                }
            }
        }
    }

}