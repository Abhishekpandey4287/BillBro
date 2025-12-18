//package com.example.billbro.screens.dialog
//
//import androidx.fragment.app.DialogFragment
//import com.example.billbro.data.entity.UserEntity
//
//class MemberPickerDialog(
//    private val availableUsers: List<UserEntity>,
//    private val onMembersSelected: (List<String>) -> Unit
//) : DialogFragment() {
//
//}
//
//// Update GroupActivity to show member picker
//binding.btnCreateGroup.setOnClickListener {
//    val name = binding.etGroupName.text.toString().trim()
//    if (name.isNotBlank()) {
//        // Show dialog to pick members
//        MemberPickerDialog(availableUsers) { selectedMembers ->
//            val allMembers = selectedMembers + currentUserId
//            vm.addGroup(
//                name = name,
//                createdBy = currentUserId,
//                members = allMembers.distinct()
//            )
//        }.show(supportFragmentManager, "member_picker")
//    }
//}