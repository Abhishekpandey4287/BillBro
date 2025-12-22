package com.example.billbro.screens.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.billbro.data.entity.MemberUI
import com.example.billbro.databinding.ItemCheckboxSelectBinding

class NameCheckboxAdapter(
    private val onItemChecked: (Int, Boolean) -> Unit
) : ListAdapter<MemberUI, NameCheckboxAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCheckboxSelectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemCheckboxSelectBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MemberUI) {
            binding.textName.text = item.name

            binding.checkboxName.setOnCheckedChangeListener(null)
            binding.checkboxName.isChecked = item.isSelected

            binding.checkboxName.setOnCheckedChangeListener { _, isChecked ->
                onItemChecked(adapterPosition, isChecked)
            }

            binding.root.setOnClickListener {
                binding.checkboxName.toggle()
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MemberUI>() {
        override fun areItemsTheSame(oldItem: MemberUI, newItem: MemberUI) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: MemberUI, newItem: MemberUI) =
            oldItem == newItem
    }
}