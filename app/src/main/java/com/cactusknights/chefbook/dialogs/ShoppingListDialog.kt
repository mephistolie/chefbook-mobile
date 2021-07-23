package com.cactusknights.chefbook.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.cactusknights.chefbook.databinding.DialogShoppingListBinding
import com.cactusknights.chefbook.fragments.ShoppingListFragment

class ShoppingListDialog(): DialogFragment() {

    private lateinit var binding: DialogShoppingListBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogShoppingListBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnAddItem.setOnClickListener {
            val itemText = binding.inputItem.text.toString()
            if (itemText.isNotEmpty()) {
                var shoppingListFragment = requireActivity().supportFragmentManager.findFragmentByTag("Shopping List")
                if (shoppingListFragment != null) {
                    shoppingListFragment = shoppingListFragment as ShoppingListFragment
                    shoppingListFragment.shoppingList.add(itemText)
                    shoppingListFragment.shoppingAdapter.notifyItemInserted(shoppingListFragment.shoppingList.size-1)
                    shoppingListFragment.binding.textEmptyList.visibility = if (shoppingListFragment.shoppingList.size > 0) View.GONE else View.VISIBLE
                }
            }
            dialog.dismiss()
        }

        return dialog
    }
}