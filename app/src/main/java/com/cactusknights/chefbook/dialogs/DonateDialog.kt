package com.cactusknights.chefbook.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.cactusknights.chefbook.databinding.DialogDonateBinding
import com.cactusknights.chefbook.helpers.Utils
import com.cactusknights.chefbook.viewmodels.UserViewModel

class DonateDialog: DialogFragment() {

    private val viewModel by activityViewModels<UserViewModel>()

    private lateinit var binding: DialogDonateBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogDonateBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnSmallDonation.setOnClickListener { viewModel.buyPremium("small_donation", requireActivity() as AppCompatActivity) }
        binding.btnMiddleDonation.setOnClickListener { viewModel.buyPremium("middle_donation", requireActivity() as AppCompatActivity) }
        binding.btnBigDonation.setOnClickListener { viewModel.buyPremium("big_donation", requireActivity() as AppCompatActivity) }
        binding.textSupport.setOnClickListener { Utils.sendEmail(requireContext()) }

        return dialog
    }
}