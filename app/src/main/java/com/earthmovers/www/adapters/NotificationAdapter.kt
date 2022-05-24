package com.earthmovers.www.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmovers.www.data.Notification
import com.earthmovers.www.databinding.NotificationListItemBinding

class NotificationAdapter :
    ListAdapter<Notification, NotificationAdapter.NotificationViewHolder>(NotificationDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = getItem(position)
        holder.bind(notification)
    }

    class NotificationViewHolder constructor(private val binding: NotificationListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {
            binding.notificationDetails.text = notification.details
            binding.date.text = notification.time
        }

        companion object {
            fun from(parent: ViewGroup): NotificationViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NotificationListItemBinding.inflate(layoutInflater, parent, false)
                return NotificationViewHolder(binding)
            }
        }
    }

    companion object NotificationDiffCallback : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.details == newItem.details
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }
}
