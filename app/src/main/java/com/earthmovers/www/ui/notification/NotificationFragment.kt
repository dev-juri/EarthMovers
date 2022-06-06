package com.earthmovers.www.ui.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.earthmovers.www.R
import com.earthmovers.www.adapters.NotificationAdapter
import com.earthmovers.www.data.remote.NotificationBody
import com.earthmovers.www.databinding.FragmentNotificationBinding
import com.earthmovers.www.ui.viewmodels.NotificationsViewModel
import com.earthmovers.www.utils.BottomNavTopLevelFragment
import com.earthmovers.www.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BottomNavTopLevelFragment(R.layout.fragment_notification) {

    private val binding by viewBinding(FragmentNotificationBinding::bind)
    private lateinit var adapter: NotificationAdapter
    private val viewModel: NotificationsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                val body = NotificationBody(it.id)
                viewModel.fetchNotifications(body)
            }
        }

        adapter = NotificationAdapter()

        binding.notificationRecyclerView.adapter = adapter

        viewModel.notifications.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            } else {
                adapter.submitList(emptyList())
            }
        }

    }
}