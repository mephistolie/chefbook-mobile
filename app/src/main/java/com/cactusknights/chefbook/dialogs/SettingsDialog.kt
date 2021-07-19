package com.cactusknights.chefbook.dialogs

import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.viewmodels.UserViewModel

class SettingsDialog: DialogFragment() {

    private val viewModel by activityViewModels<UserViewModel>()
    private lateinit var sp: SharedPreferences

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        sp = requireActivity().getSharedPreferences("ChefBook", AppCompatActivity.MODE_PRIVATE)

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_settings)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val userName = dialog.findViewById<TextView>(R.id.user)
        val premiumBadge = dialog.findViewById<ImageButton>(R.id.premium)
        val logoutButton = dialog.findViewById<Button>(R.id.logout)
        val aboutButton = dialog.findViewById<LinearLayoutCompat>(R.id.about_app)
        val supportButton = dialog.findViewById<LinearLayoutCompat>(R.id.buy_premium)

        val displayName = viewModel.getCurrentUser().value?.displayName.toString()
        userName.text = if (displayName.isNotEmpty()) displayName else viewModel.getCurrentUser().value?.email.toString()

        val shoppingListDefault = dialog.findViewById<SwitchCompat>(R.id.shopping_list_default)
        shoppingListDefault.isChecked = sp.getBoolean("shoppingListIsDefault", false)
        shoppingListDefault.setOnClickListener {
            sp.edit().putBoolean("shoppingListIsDefault", !sp.getBoolean("shoppingListIsDefault", false)).apply()
        }

        val currentUser = viewModel.getCurrentUser().value
        if (currentUser != null && currentUser.isPremium) {
            premiumBadge.visibility = View.VISIBLE
        }

        aboutButton.setOnClickListener {
            AboutDialog().show(requireActivity().supportFragmentManager, "About")
            dialog.dismiss()
        }
        supportButton.setOnClickListener {
            DonateDialog().show(requireActivity().supportFragmentManager, "Donate")
            dialog.dismiss()
        }
        logoutButton.setOnClickListener {
            ConfirmDialog { viewModel.logout() }.show(requireActivity().supportFragmentManager, "Confirm")
            dialog.dismiss()
        }

        return dialog
    }
}