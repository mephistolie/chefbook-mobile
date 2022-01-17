package com.cactusknights.chefbook.screens.main.fragments.profile.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.cactusknights.chefbook.databinding.DialogBroccoinsBinding

class BroccoinsDialog: DialogFragment() {

    private lateinit var binding: DialogBroccoinsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogBroccoinsBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }
}