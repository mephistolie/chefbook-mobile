package com.cactusknights.chefbook.screens.main.fragments.profile.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.cactusknights.chefbook.databinding.DialogInfoBinding
import com.cactusknights.chefbook.common.Utils

class AboutDialog: DialogFragment() {

    private lateinit var binding: DialogInfoBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogInfoBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.textSupport.setOnClickListener {
            Utils.sendEmail(requireContext())
        }

        binding.cvVk.setOnClickListener {
            Utils.openVkGroup(requireContext())
        }

        return dialog
    }
}