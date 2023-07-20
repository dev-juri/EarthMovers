package com.earthmovers.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmovers.app.data.domain.RecentProject
import com.earthmovers.app.databinding.ProjectListItemBinding

class RecentProjectsAdapter(
    private val onPostClickListener: PostClickListener
) :
    ListAdapter<RecentProject, RecentProjectsAdapter.RecentProjectsViewHolder>(
        RecentProjectsDiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentProjectsViewHolder {
        return RecentProjectsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecentProjectsViewHolder, position: Int) {
        val recentProject = getItem(position)
        holder.bind(recentProject)
        holder.binding.viewDetails.setOnClickListener {
            onPostClickListener.onPostClick(recentProject)
        }
    }

    class RecentProjectsViewHolder constructor(val binding: ProjectListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recentProject: RecentProject) {
            binding.projectDetails.text = recentProject.projectHighlight
            binding.projectLocation.text = recentProject.location
        }

        companion object {
            fun from(parent: ViewGroup): RecentProjectsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProjectListItemBinding.inflate(layoutInflater, parent, false)
                return RecentProjectsViewHolder(binding)
            }
        }
    }

    companion object RecentProjectsDiffCallback : DiffUtil.ItemCallback<RecentProject>() {
        override fun areItemsTheSame(oldItem: RecentProject, newItem: RecentProject): Boolean {
            return oldItem.projectHighlight == newItem.projectHighlight
        }

        override fun areContentsTheSame(oldItem: RecentProject, newItem: RecentProject): Boolean {
            return oldItem == newItem
        }
    }
}
