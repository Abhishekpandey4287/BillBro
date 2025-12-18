package com.example.billbro.screens.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billbro.R
import com.example.billbro.data.entity.ExpenseEntity
import com.example.billbro.databinding.ActivityMainBinding
import com.example.billbro.screens.adapter.ExpenseAdapter
import com.example.billbro.screens.dialog.AddExpenseDialogFragment
import com.example.billbro.viewmodel.ExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ExpenseViewModel by viewModels()
    private lateinit var adapter: ExpenseAdapter

    private var currentGroupId: String = ""

    private val currentUserId by lazy {
        com.google.firebase.auth.FirebaseAuth
            .getInstance().currentUser?.uid
            ?: error("User not logged in")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentGroupId = intent.getStringExtra("groupId") ?: "demo_group"

        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        viewModel.loadBalance(currentGroupId, currentUserId)
        viewModel.loadExpenses(currentGroupId)

        binding.btnAdd.setOnClickListener {
            AddExpenseDialogFragment
                .newInstance(currentGroupId)
                .show(supportFragmentManager, "add_expense")
        }
    }

    private fun setupRecyclerView() {
        adapter = ExpenseAdapter(
            onExpenseClick = { expense ->
                showExpenseDetails(expense)
            },
            onDeleteClick = { expense ->
                showDeleteExpenseDialog(expense)
            }
        )
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.expenses.collect { expenses ->
                    adapter.submitList(expenses)
                    binding.recycler.visibility =
                        if (expenses.isEmpty()) View.GONE else View.VISIBLE
                    updatePaidBySummary(expenses)
                }
            }
        }
    }

    private fun showDeleteExpenseDialog(expense: ExpenseEntity) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Delete expense")
            .setMessage("Do you want to delete this Expense?")
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.deleteExpense(expense.expenseId)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun updatePaidBySummary(expenses: List<ExpenseEntity>) {
        val container = binding.layoutPaidBySummary
        container.removeAllViews()

        val balances = calculateBalances(expenses)

        val summaryBuilder = android.text.SpannableStringBuilder()

        balances.forEach { (name, amount) ->

            val displayText: CharSequence =
                if (amount > 0) {
                    val raw = "$name gets ₹${"%.2f".format(amount)}\n"
                    createColoredAmountText(
                        raw,
                        getColor(android.R.color.holo_green_light)
                    )
                } else if (amount < 0) {
                    val raw = "$name owes ₹${"%.2f".format(-amount)}\n"
                    createColoredAmountText(
                        raw,
                        getColor(android.R.color.holo_red_dark)
                    )
                } else {
                    "$name is settled up\n"
                }

            val textView = android.widget.TextView(this).apply {
                textSize = 14f
                setPadding(16, 8, 16, 8)
                text = displayText
            }

            container.addView(textView)
            summaryBuilder.append(displayText)

        }
        com.example.billbro.utils.GroupSummaryStore.put(
            currentGroupId,
            summaryBuilder
        )
    }

    private fun createColoredAmountText(
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
    private fun setupClickListeners() {
        binding.btnAdd.setOnClickListener {
            showAddExpenseDialog()
        }
    }

    private fun showAddExpenseDialog() {
        AddExpenseDialogFragment.newInstance(currentGroupId)
            .show(supportFragmentManager, "add_expense")
    }

    private fun showExpenseDetails(expense: ExpenseEntity) {
        // Navigate to expense detail screen
        // TODO: Implement ExpenseDetailActivity
        // val intent = Intent(this, ExpenseDetailActivity::class.java).apply {
        //     putExtra("expense_id", expense.expenseId)a
        // }
        // startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_balance -> {
                showBalances()
                true
            }
            R.id.action_settle -> {
                showSettleUp()
                true
            }
            R.id.action_settings -> {
                showSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showBalances() {
    }

    private fun showSettleUp() {
        val intent = android.content.Intent(this, SettleActivity::class.java)
        startActivity(intent)
    }

    private fun showSettings() {
    }
}