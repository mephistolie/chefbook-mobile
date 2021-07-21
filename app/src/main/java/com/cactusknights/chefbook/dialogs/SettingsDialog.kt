package com.cactusknights.chefbook.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.cactusknights.chefbook.databinding.DialogSettingsBinding
import com.cactusknights.chefbook.viewmodels.UserViewModel

class SettingsDialog: DialogFragment() {

    private val viewModel by activityViewModels<UserViewModel>()
    private lateinit var sp: SharedPreferences

    private lateinit var binding: DialogSettingsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        sp = requireActivity().getSharedPreferences("ChefBook", AppCompatActivity.MODE_PRIVATE)

        binding = DialogSettingsBinding.inflate(requireActivity().layoutInflater)
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val displayName = viewModel.getCurrentUser()?.displayName.toString()
        binding.textUser.text = if (displayName.isNotEmpty()) displayName else viewModel.getCurrentUser()?.email.toString()

        binding.switchShoppingListDefault.isChecked = sp.getBoolean("shoppingListIsDefault", false)
        binding.switchShoppingListDefault.setOnClickListener {
            sp.edit().putBoolean("shoppingListIsDefault", !sp.getBoolean("shoppingListIsDefault", false)).apply()
        }

        val currentUser = viewModel.getCurrentUser()
        if (currentUser != null && currentUser.isPremium) {
            binding.imagePremium.visibility = View.VISIBLE
        }

        binding.llAbout.setOnClickListener {
            AboutDialog().show(requireActivity().supportFragmentManager, "About")
            dialog.dismiss()
        }
        binding.llDonate.setOnClickListener {
            DonateDialog().show(requireActivity().supportFragmentManager, "Donate")
            dialog.dismiss()
        }
        binding.btnLogout.setOnClickListener {
            ConfirmDialog { viewModel.logout() }.show(requireActivity().supportFragmentManager, "Confirm")
            dialog.dismiss()
        }

        return dialog
    }
}