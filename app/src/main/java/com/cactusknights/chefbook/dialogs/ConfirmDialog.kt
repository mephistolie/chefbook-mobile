package com.cactusknights.chefbook.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.cactusknights.chefbook.databinding.DialogConfirmBinding

class ConfirmDialog(val action: () -> Unit): DialogFragment() {

    private lateinit var binding: DialogConfirmBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogConfirmBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnConfirm.setOnClickListener {
            dialog.dismiss()
            action()
        }

        return dialog
    }
}