package com.cactusknights.chefbook.screens.main.fragments.editprofile.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.cactusknights.chefbook.databinding.DialogNameInputBinding

class ChangeNameDialog(val username: String = "", val action: (String) -> Unit): DialogFragment() {

    private lateinit var binding: DialogNameInputBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogNameInputBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.inputName.setText(username)

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnConfirm.setOnClickListener {
            dialog.dismiss()
            action(binding.inputName.text.toString())
        }

        return dialog
    }
}