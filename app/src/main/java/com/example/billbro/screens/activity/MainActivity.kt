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
//        expenseAdapter = ExpenseAdapter { expense ->
//            showExpenseDetails(expense)
//        }
//
//        binding.recycler.apply {
//            layoutManager = LinearLayoutManager(this@MainActivity)
//            adapter = expenseAdapter
//            setHasFixedSize(true)
//        }
        adapter = ExpenseAdapter {}
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.expenses.collect { expenses ->
                    adapter.submitList(expenses)
                    binding.emptyState.visibility =
                        if (expenses.isEmpty()) View.VISIBLE else View.GONE
                    binding.recycler.visibility =
                        if (expenses.isEmpty()) View.GONE else View.VISIBLE
                }
            }
        }
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
        // TODO: Implement BalanceActivity
        // val intent = Intent(this, BalanceActivity::class.java)
        // startActivity(intent)
    }

    private fun showSettleUp() {
        val intent = android.content.Intent(this, SettleActivity::class.java)
        startActivity(intent)
    }

    private fun showSettings() {
        // Implement settings
    }
}