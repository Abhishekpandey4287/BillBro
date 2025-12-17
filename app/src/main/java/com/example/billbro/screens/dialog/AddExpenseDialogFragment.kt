package com.example.billbro.screens.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.billbro.databinding.DialogAddExpenseBinding
import com.example.billbro.viewmodel.ExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExpenseDialogFragment : DialogFragment() {

    private var _binding: DialogAddExpenseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpenseViewModel by activityViewModels()
    private var groupId: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())
        _binding = DialogAddExpenseBinding.inflate(inflater, null, false)

        arguments?.let {
            groupId = it.getString("group_id") ?: ""
        }

        setupClickListeners()

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Add Expense")
            .create()
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

        viewModel.addExpense(
            groupId = groupId,
            amount = amount,
            paidBy = paidBy,
            description = description
        )

        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(groupId: String): AddExpenseDialogFragment {
            return AddExpenseDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("group_id", groupId)
                }
            }
        }
    }
}