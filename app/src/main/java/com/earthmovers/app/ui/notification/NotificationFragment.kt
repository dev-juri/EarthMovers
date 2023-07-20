package com.earthmovers.app.ui.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.earthmovers.app.R
import com.earthmovers.app.adapters.NotificationAdapter
import com.earthmovers.app.adapters.NotificationClickListener
import com.earthmovers.app.data.remote.GetUserBody
import com.earthmovers.app.data.remote.NotificationBody
import com.earthmovers.app.databinding.FragmentNotificationBinding
import com.earthmovers.app.ui.viewmodels.NotificationsViewModel
import com.earthmovers.app.utils.BottomNavTopLevelFragment
import com.earthmovers.app.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BottomNavTopLevelFragment(R.layout.fragment_notification),
    NotificationClickListener {

    private val binding by viewBinding(FragmentNotificationBinding::bind)
    private lateinit var adapter: NotificationAdapter
    private val viewModel: NotificationsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                val body = NotificationBody(it.id)
                viewModel.fetchNotifications(body)
            }
        }

        adapter = NotificationAdapter(this)

        binding.notificationRecyclerView.adapter = adapter

        viewModel.notifications.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            } else {
                adapter.submitList(emptyList())
            }
        }

    }

    override fun checkProfile(id: String) {
        viewModel.getUserWithId(GetUserBody(userID = id))
        findNavController().navigate(R.id.action_notificationFragment_to_notificationProfileDetailsFragment)
    }
}