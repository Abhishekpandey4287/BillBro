package com.example.billbro.screens.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.billbro.data.entity.GroupEntity
import com.example.billbro.databinding.ItemGroupBinding

class GroupAdapter(
    private val onItemClick: (GroupEntity) -> Unit
) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    private val groups = mutableListOf<GroupEntity>()

    inner class GroupViewHolder(
        private val binding: ItemGroupBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(group: GroupEntity) {
            binding.tvGroupName.text = group.groupName

            val summary =
                com.example.billbro.utils.GroupSummaryStore.get(group.groupId)

            binding.tvBalanceSummary.text = summary ?: "You are settled up"

            binding.root.setOnClickListener {
                onItemClick(group)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = ItemGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    override fun getItemCount(): Int = groups.size

    fun submit(list: List<GroupEntity>) {
        groups.clear()
        groups.addAll(list)
        notifyDataSetChanged()
    }
}