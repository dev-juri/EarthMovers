package com.earthmovers.www.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmovers.www.data.domain.DomainNotification
import com.earthmovers.www.databinding.NotificationListItemBinding
import com.earthmovers.www.utils.getFormattedDate
import com.earthmovers.www.utils.getFormattedTime

class NotificationAdapter(
    private val notificationClickListener: NotificationClickListener
) :
    ListAdapter<DomainNotification, NotificationAdapter.NotificationViewHolder>(
        NotificationDiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = getItem(position)
        holder.bind(notification)
        holder.binding.checkProfile.setOnClickListener {
            notificationClickListener.checkProfile(notification.ID)
        }
    }

    class NotificationViewHolder constructor(val binding: NotificationListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(domainNotification: DomainNotification) {
            binding.notificationDetails.text = domainNotification.details
            binding.date.text = "${getFormattedTime(domainNotification.time)} ${getFormattedDate(domainNotification.time)}"
        }

        companion object {
            fun from(parent: ViewGroup): NotificationViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NotificationListItemBinding.inflate(layoutInflater, parent, false)
                return NotificationViewHolder(binding)
            }
        }
    }

    companion object NotificationDiffCallback : DiffUtil.ItemCallback<DomainNotification>() {
        override fun areItemsTheSame(
            oldItem: DomainNotification,
            newItem: DomainNotification
        ): Boolean {
            return oldItem.details == newItem.details
        }

        override fun areContentsTheSame(
            oldItem: DomainNotification,
            newItem: DomainNotification
        ): Boolean {
            return oldItem == newItem
        }
    }
}
