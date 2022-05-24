package com.earthmovers.www.ui.notification

import android.os.Bundle
import android.view.View
import com.earthmovers.www.R
import com.earthmovers.www.adapters.NotificationAdapter
import com.earthmovers.www.data.Notification
import com.earthmovers.www.databinding.FragmentNotificationBinding
import com.earthmovers.www.utils.BottomNavTopLevelFragment
import com.earthmovers.www.utils.viewBinding

class NotificationFragment: BottomNavTopLevelFragment(R.layout.fragment_notification) {

    private val binding by viewBinding(FragmentNotificationBinding::bind)
    private lateinit var adapter: NotificationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notificationList = listOf<Notification>(
            Notification("Sam Perry accepted your offer", "10:06am 24th May 2022"),
            Notification("Tola Perry accepted your offer", "13:06am 24th May 2022"),
            Notification("Samson Perry accepted your offer", "11:06am 24th May 2022")
        )
        adapter = NotificationAdapter()
        adapter.submitList(notificationList)

        binding.notificationRecyclerView.adapter = adapter
    }
}