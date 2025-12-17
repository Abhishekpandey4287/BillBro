package com.example.billbro.screens.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.billbro.data.entity.UserBalance
import com.example.billbro.databinding.ItemBalanceBinding

class BalanceAdapter(
    private val onUserClick: (UserBalance) -> Unit
) : ListAdapter<UserBalance, BalanceAdapter.BalanceViewHolder>(BalanceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder {
        val binding = ItemBalanceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BalanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BalanceViewHolder(
        private val binding: ItemBalanceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(balance: UserBalance) {
            binding.apply {
                tvUserName.text = balance.userId
                tvAmount.text = "₹${String.format("%.2f", Math.abs(balance.total))}"

                when {
                    balance.total > 0 -> {
                        // User gets money
                        tvBalanceStatus.text = "You get ${String.format("%.2f", balance.total)}"
                        tvAmount.setTextColor(binding.root.context.getColor(android.R.color.holo_green_dark))
                    }
                    balance.total < 0 -> {
                        // User owes money
                        tvBalanceStatus.text = "You owe ${String.format("%.2f", Math.abs(balance.total))}"
                        tvAmount.setTextColor(binding.root.context.getColor(android.R.color.holo_red_dark))
                    }
                    else -> {
                        // Settled up
                        tvBalanceStatus.text = "All settled up"
                        tvAmount.text = "₹0"
                        tvAmount.setTextColor(binding.root.context.getColor(android.R.color.darker_gray))
                    }
                }

                root.setOnClickListener {
                    onUserClick(balance)
                }
            }
        }
    }

    class BalanceDiffCallback : DiffUtil.ItemCallback<UserBalance>() {
        override fun areItemsTheSame(oldItem: UserBalance, newItem: UserBalance): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: UserBalance, newItem: UserBalance): Boolean {
            return oldItem == newItem
        }
    }
}