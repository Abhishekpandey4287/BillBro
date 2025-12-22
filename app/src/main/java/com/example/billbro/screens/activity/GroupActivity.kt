package com.example.billbro.screens.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billbro.data.entity.ExpenseEntity
import com.example.billbro.data.entity.GroupEntity
import com.example.billbro.data.repository.ExpenseRepository
import com.example.billbro.data.repository.GroupSummaryRepository
import com.example.billbro.databinding.ActivityGroupBinding
import com.example.billbro.screens.adapter.GroupAdapter
import com.example.billbro.viewmodel.GroupViewModel
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupActivity : AppCompatActivity()  {
    private val vm: GroupViewModel by viewModels()
    private lateinit var binding: ActivityGroupBinding

    private lateinit var adapter: GroupAdapter

    private val activeGroupCollectors = mutableSetOf<String>()

    @Inject
    lateinit var summaryRepo: GroupSummaryRepository

    @Inject
    lateinit var expenseRepository: ExpenseRepository

    private var summaryMap: Map<String, CharSequence> = emptyMap()

    private val currentUserId: String
        get() = com.google.firebase.auth.FirebaseAuth
            .getInstance()
            .currentUser?.uid
            ?: throw IllegalStateException("User not logged in")

//    override fun onResume() {
//        super.onResume()
//        adapter.notifyDataSetChanged()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = GroupAdapter(
            onItemClick = { group ->
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("groupId", group.groupId)
                startActivity(intent)
            },
            onDeleteClick = { group ->
                showDeleteExpenseDialog(group)
            }
        )
        binding.groupRecycler.layoutManager = LinearLayoutManager(this)
        binding.groupRecycler.adapter = adapter

        vm.getGroups(currentUserId).observe(this) { groups ->
            adapter.submit(groups)

            groups.forEach { group ->
                if (activeGroupCollectors.contains(group.groupId)) return@forEach

                activeGroupCollectors.add(group.groupId)

                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        expenseRepository
                            .getExpenses(group.groupId)
                            .collect { expenses ->
                                if (expenses.isNotEmpty()) {
                                    val summary = buildSummary(expenses)
                                    summaryRepo.update(group.groupId, summary)
                                }
                            }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                summaryRepo.summaryFlow.collect { summaries ->
                    adapter.updateSummaries(summaries)
                }
            }
        }

        binding.btnCreateGroup.setOnClickListener {
            val name = binding.etGroupName.text.toString().trim()
            if (name.isNotBlank()) {
                vm.addGroup(
                    name = name,
                    createdBy = currentUserId,
                    members = listOf(currentUserId)
                )
                binding.etGroupName.text.clear()
            }
        }
    }

    private fun coloredText(
        raw: String,
        color: Int
    ): CharSequence {
        val spannable = android.text.SpannableString(raw)
        val start = raw.indexOf("₹")

        if (start != -1) {
            spannable.setSpan(
                android.text.style.ForegroundColorSpan(color),
                start,
                raw.length,
                android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                start,
                raw.length,
                android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannable
    }

    private fun buildSummary(
        expenses: List<ExpenseEntity>
    ): CharSequence {

        val balances = calculateBalances(expenses)
        val builder = android.text.SpannableStringBuilder()

        balances.forEach { (name, amount) ->
            val text: CharSequence =
                if (amount > 0) {
                    coloredText(
                        "$name gets ₹${"%.2f".format(amount)}\n",
                        getColor(android.R.color.holo_green_light)
                    )
                } else if (amount < 0) {
                    coloredText(
                        "$name owes ₹${"%.2f".format(-amount)}\n",
                        getColor(android.R.color.holo_red_dark)
                    )
                } else {
                    "$name is settled up\n"
                }

            builder.append(text)
        }

        return builder
    }


    private fun calculateBalances(expenses: List<ExpenseEntity>): Map<String, Double> {
        if (expenses.isEmpty()) return emptyMap()

        val users = expenses
            .flatMap { listOf(it.paidBy) }
            .distinct()

        val balanceMap = mutableMapOf<String, Double>()

        users.forEach { balanceMap[it] = 0.0 }

        expenses.forEach { expense ->
            val splitCount = users.size
            if (splitCount == 0) return@forEach

            val share = expense.amount / splitCount

            balanceMap[expense.paidBy] =
                balanceMap.getValue(expense.paidBy) + (expense.amount - share)

            users.forEach { user ->
                if (user != expense.paidBy) {
                    balanceMap[user] =
                        balanceMap.getValue(user) - share
                }
            }
        }

        return balanceMap
    }

    private fun showDeleteExpenseDialog(group: GroupEntity) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Delete expense")
            .setMessage("Do you want to delete this Group?")
            .setPositiveButton("Yes") { dialog, _ ->
                vm.deleteGroup(group.groupId)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}