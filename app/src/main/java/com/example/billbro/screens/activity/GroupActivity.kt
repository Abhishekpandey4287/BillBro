package com.example.billbro.screens.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billbro.databinding.ActivityGroupBinding
import com.example.billbro.screens.adapter.GroupAdapter
import com.example.billbro.viewmodel.GroupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupActivity : AppCompatActivity()  {
    private val vm: GroupViewModel by viewModels()
    private lateinit var binding: ActivityGroupBinding

    private lateinit var adapter: GroupAdapter

    private val currentUserId: String
        get() = com.google.firebase.auth.FirebaseAuth
            .getInstance()
            .currentUser?.uid
            ?: throw IllegalStateException("User not logged in")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = GroupAdapter { group ->
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("groupId", group.groupId)
            startActivity(intent)
        }
        binding.groupRecycler.layoutManager = LinearLayoutManager(this)
        binding.groupRecycler.adapter = adapter
        vm.getGroups(currentUserId).observe(this) {
            adapter.submit(it)
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
}