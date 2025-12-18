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

    @Inject
    lateinit var summaryRepo: GroupSummaryRepository

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

        vm.getGroups(currentUserId).observe(this) {groups ->
            adapter.submit(groups)
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