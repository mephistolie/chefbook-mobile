package com.cactusknights.chefbook.helpers

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageButton
import android.widget.TextView
import com.cactusknights.chefbook.R


object Dialogs {

    fun getConfirmDialog(context: Context, action: () -> Unit) {
        val dialog = Dialog(context)
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
        dialog.show()
    }

    fun openAboutDialog(context: Context, function: () -> Unit) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_info)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val support = dialog.findViewById<TextView>(R.id.support)
        support.setOnClickListener {
            function()
        }
        dialog.show()
    }

    fun openGratitudeDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_gratitude)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}