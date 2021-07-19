package com.cactusknights.chefbook.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.cactusknights.chefbook.R

class ConfirmDialog(val action: () -> Unit): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_confirm)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val cancel = dialog.findViewById<ImageButton>(R.id.cancel)
        val confirm = dialog.findViewById<ImageButton>(R.id.confirm)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        confirm.setOnClickListener {
            action()
            dialog.dismiss()
        }

        return dialog
    }
}