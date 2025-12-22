package com.example.billbro.screens.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.billbro.data.entity.MemberUI
import com.example.billbro.data.repository.SplitType
import com.example.billbro.databinding.DialogAddExpenseBinding
import com.example.billbro.screens.adapter.NameCheckboxAdapter
import com.example.billbro.viewmodel.ExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExpenseDialogFragment : DialogFragment() {

    private var _binding: DialogAddExpenseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpenseViewModel by activityViewModels()
    private var groupId: String = ""
    private var availableNames: List<String> = emptyList()

    private lateinit var dropdownAdapter: NameCheckboxAdapter
    private val memberList = mutableListOf<MemberUI>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())
        _binding = DialogAddExpenseBinding.inflate(inflater, null, false)

        arguments?.let {
            groupId = it.getString("group_id") ?: ""
            availableNames = it.getStringArrayList("available_names") ?: emptyList()
        }

        setupDropdown()
        setupClickListeners()

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Add Expense")
            .create()
    }

    private fun setupDropdown() {
        if (availableNames.isEmpty()) {
            binding.tilSplitBetween.visibility = android.view.View.GONE
            return
        }

        memberList.clear()
        memberList.addAll(availableNames.map { MemberUI(it, true) })

        updateSelectedText()

        binding.etSplitBetween.setOnClickListener {
            showSplitBetweenDialog()
        }
    }

    private fun showSplitBetweenDialog() {
        dropdownAdapter = NameCheckboxAdapter { position, isChecked ->
            memberList[position] = memberList[position].copy(isSelected = isChecked)
        }

        dropdownAdapter.submitList(memberList.toList())

        val recyclerView = androidx.recyclerview.widget.RecyclerView(requireContext()).apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            this.adapter = dropdownAdapter
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Split Between")
            .setView(recyclerView)
            .setPositiveButton("Done") { _, _ ->
                updateSelectedText()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateSelectedText() {
        val selected = memberList.filter { it.isSelected }.map { it.name }

        binding.etSplitBetween.setText(
            when {
                selected.isEmpty() -> "Split equally"
                selected.size == memberList.size -> "Split equally"
                else -> selected.joinToString(",")
            }
        )
    }

    private fun setupClickListeners() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            addExpense()
        }
    }

    private fun addExpense() {
        val amount = binding.etAmount.text?.toString()?.toDoubleOrNull()
        val description = binding.etDescription.text?.toString().orEmpty()
        val paidBy = binding.etPaidBy.text?.toString().orEmpty()
//        val splitBetweenText = binding.etSplitBetween.text?.toString().orEmpty()

        binding.etAmount.error = null
        binding.etDescription.error = null
        binding.etPaidBy.error = null

        when {
            amount == null || amount <= 0 -> {
                binding.etAmount.error = "Enter a valid amount"
                return
            }
            description.isBlank() -> {
                binding.etDescription.error = "Enter description"
                return
            }
            paidBy.isBlank() -> {
                binding.etPaidBy.error = "Enter who paid"
                return
            }
        }

        val selectedMembers =
            memberList.filter { it.isSelected }.map { it.name }

        val splitType =
            if (selectedMembers.isNotEmpty() &&
                selectedMembers.size < memberList.size
            ) {
                SplitType.BETWEEN
            } else {
                SplitType.EQUAL
            }

        val effectiveSplitBetween =
            if (splitType == SplitType.BETWEEN) {
                selectedMembers.filter { it != paidBy }
            } else {
                emptyList()
            }

        viewModel.addExpense(
            groupId = groupId,
            amount = amount,
            paidBy = paidBy,
            description = description,
            splitBetween = effectiveSplitBetween,
            splitType = splitType
        )

        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(groupId: String,  availableNames: List<String>): AddExpenseDialogFragment {
            return AddExpenseDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("group_id", groupId)
                    putStringArrayList("available_names", ArrayList(availableNames))
                }
            }
        }
    }
}