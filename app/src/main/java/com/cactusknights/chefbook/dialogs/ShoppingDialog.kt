package com.cactusknights.chefbook.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.fragments.ShoppingListFragment

class ShoppingDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_shopping_list)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val addItem = dialog.findViewById<AppCompatButton>(R.id.add_item)
        val item = dialog.findViewById<TextView>(R.id.item)
        addItem.setOnClickListener {
            val itemText = item.text.toString()
            if (itemText.isNotEmpty()) {
                var shoppingListFragment = requireActivity().supportFragmentManager.findFragmentByTag("Shopping List")
                if (shoppingListFragment != null) {
                    shoppingListFragment = shoppingListFragment as ShoppingListFragment
                    shoppingListFragment.shoppingList.add(itemText)
                    shoppingListFragment.shoppingAdapter.notifyItemInserted(shoppingListFragment.shoppingList.size-1)
                    shoppingListFragment.emptyListTitle.visibility = if (shoppingListFragment.shoppingList.size > 0) View.GONE else View.VISIBLE
                }
            }
            dialog.dismiss()
        }

        return dialog
    }
}