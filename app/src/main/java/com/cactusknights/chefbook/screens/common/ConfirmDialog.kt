package com.cactusknights.chefbook.screens.common

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.cactusknights.chefbook.databinding.DialogConfirmBinding

class ConfirmDialog(private val descriptionId: Int? = null, val action: () -> Unit): DialogFragment() {

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

        if (descriptionId != null) {
            binding.textDescription.text = requireContext().resources.getString(descriptionId)
        } else {
            binding.llDescription.visibility = View.GONE
        }

        return dialog
    }
}