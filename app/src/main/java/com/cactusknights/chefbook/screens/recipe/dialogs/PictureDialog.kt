package com.cactusknights.chefbook.screens.recipe.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.cactusknights.chefbook.common.forceSubmitList
import com.cactusknights.chefbook.databinding.DialogCategoriesBinding
import com.cactusknights.chefbook.databinding.DialogPictureBinding
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.screens.recipe.RecipeViewModel
import com.cactusknights.chefbook.screens.recipe.adapters.RecipeCategoryAdapter
import com.cactusknights.chefbook.screens.recipe.models.RecipeScreenEvent
import com.cactusknights.chefbook.screens.recipe.models.RecipeScreenState
import kotlinx.coroutines.flow.collect
import java.util.*

class PictureDialog(val uri: String) : DialogFragment() {

    private lateinit var binding: DialogPictureBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        binding = DialogPictureBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(context).setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        binding.ivPicture.load(uri)
        return dialog
    }
}