package com.cactusknights.chefbook.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.helpers.Utils
import com.cactusknights.chefbook.viewmodels.UserViewModel

class DonateDialog: DialogFragment() {

    private val viewModel by activityViewModels<UserViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_donate)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val smallDonation = dialog.findViewById<TextView>(R.id.small_donation)
        val middleDonation = dialog.findViewById<TextView>(R.id.middle_donation)
        val bigDonation = dialog.findViewById<TextView>(R.id.big_donation)
        val support = dialog.findViewById<TextView>(R.id.support)

        smallDonation.setOnClickListener { viewModel.buyPremium("small_donation", requireActivity()) }
        middleDonation.setOnClickListener { viewModel.buyPremium("middle_donation", requireActivity()) }
        bigDonation.setOnClickListener { viewModel.buyPremium("big_donation", requireActivity()) }
        support.setOnClickListener { Utils.sendEmail(requireContext()) }

        return dialog
    }
}