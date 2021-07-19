package com.cactusknights.chefbook.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.helpers.Utils

class AboutDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_info)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val support = dialog.findViewById<TextView>(R.id.support)
        support.setOnClickListener {
            Utils.sendEmail(requireContext())
        }
        dialog.show()

        return dialog
    }
}