package com.cactusknights.chefbook.screens.main.fragments.profile.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.cactusknights.chefbook.common.Utils
import com.cactusknights.chefbook.common.openMail
import com.cactusknights.chefbook.databinding.DialogDonateBinding

class SubscriptionDialog: DialogFragment() {

//    private val viewModel by activityViewModels<UuuserViewModel>()

    private lateinit var binding: DialogDonateBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogDonateBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

//        binding.btnSmallDonation.setOnClickListener { viewModel.buyPremium("small_donation", requireActivity() as AppCompatActivity) }
//        binding.btnMiddleDonation.setOnClickListener { viewModel.buyPremium("middle_donation", requireActivity() as AppCompatActivity) }
//        binding.btnBigDonation.setOnClickListener { viewModel.buyPremium("big_donation", requireActivity() as AppCompatActivity) }
        binding.textSupport.setOnClickListener { requireContext().openMail() }

        return dialog
    }
}