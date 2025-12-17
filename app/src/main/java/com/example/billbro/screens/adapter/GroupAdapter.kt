package com.example.billbro.screens.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.billbro.data.entity.GroupEntity
import com.example.billbro.data.entity.GroupSummary
import com.example.billbro.databinding.ItemGroupBinding

class GroupAdapter(private val onClick: (GroupEntity) -> Unit)
    : RecyclerView.Adapter<GroupAdapter.VH>() {
    private val list = mutableListOf<GroupEntity>()
    fun submit(data: List<GroupEntity>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
    inner class VH(val tv: TextView) : RecyclerView.ViewHolder(tv) {
        init {
            tv.setPadding(32, 32, 32, 32)
            tv.setOnClickListener { onClick(list[adapterPosition]) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(TextView(parent.context).apply { textSize = 18f })
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.tv.text = list[position].groupName
    }
    override fun getItemCount() = list.size
}
//
//class GroupAdapter(
//    private val onClick: (GroupSummary) -> Unit
//) : ListAdapter<GroupSummary, GroupAdapter.VH>(Diff()) {
//        inner class VH(val binding: ItemGroupBinding)
//            : RecyclerView.ViewHolder(binding.root)
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
//            val binding = ItemGroupBinding.inflate(
//                LayoutInflater.from(parent.context), parent, false
//            )
//            return VH(binding)
//        }
//        override fun onBindViewHolder(holder: VH, position: Int) {
//            val item = getItem(position)
//            holder.binding.apply {
//                tvGroupName.text = item.groupName
//                tvBalanceSummary.text = when {
//                    item.netBalance > 0 -> "You get ₹${item.netBalance}"
//                    item.netBalance < 0 -> "You owe ₹$
//                    { kotlin.math.abs(item.netBalance) }"
//                    else -> "All settled"
//                }
//            }
//            holder.itemView.setOnClickListener { onClick(item) }
//        }
//    }