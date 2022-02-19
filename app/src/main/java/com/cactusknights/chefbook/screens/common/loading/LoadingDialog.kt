package com.cactusknights.chefbook.screens.common.loading

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.cactusknights.chefbook.databinding.DialogLoadingBinding

class LoadingDialog: DialogFragment() {

    private lateinit var binding: DialogLoadingBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogLoadingBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(context).setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }
}