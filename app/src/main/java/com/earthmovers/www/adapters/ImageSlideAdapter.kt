package com.earthmovers.www.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.earthmovers.www.databinding.ImageSliderContainerBinding

class ImageSlideAdapter(
    private val imageSliderList: List<ImageDetails>
) : PagerAdapter() {

    private lateinit var binding: ImageSliderContainerBinding

    override fun getCount(): Int = imageSliderList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        binding = ImageSliderContainerBinding.inflate(inflater, container, false)

        imageSliderList[position].let {
            binding.image.setImageResource(it.drawableId)
            binding.imgDetails.text = it.text
        }

        val viewPager = container as ViewPager
        viewPager.addView(binding.root, 0)

        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }
}

data class ImageDetails(
    var text: String,
    var drawableId: Int
)



