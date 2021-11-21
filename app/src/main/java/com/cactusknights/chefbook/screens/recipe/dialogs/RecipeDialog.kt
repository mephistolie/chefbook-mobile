package com.cactusknights.chefbook.screens.recipe.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.DialogFragment
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.screens.recipe.RecipeActivity
import com.cactusknights.chefbook.screens.recipeinput.RecipeInputActivity
import com.cactusknights.chefbook.databinding.DialogRecipeBinding
import com.cactusknights.chefbook.common.ConfirmDialog
import com.cactusknights.chefbook.common.Utils

class RecipeDialog(val activity: RecipeActivity): DialogFragment() {

    private lateinit var binding: DialogRecipeBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogRecipeBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(activity)
            .setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (activity.viewModel.state.value.recipe.isFavourite) binding.btnFavourite.setImageResource(R.drawable.ic_favorite)
        binding.btnFavourite.setOnClickListener {
            activity.viewModel.changeRecipeFavouriteStatus()
            if (activity.viewModel.state.value.recipe.isFavourite) binding.btnFavourite.setImageResource(R.drawable.ic_favorite) else binding.btnFavourite.setImageResource(R.drawable.ic_unfavorite)
        }
        binding.btnShare.setOnClickListener {
            Utils.shareRecipe(activity.viewModel.state.value.recipe, resources, ::startActivity)
        }
        binding.btnEditRecipe.setOnClickListener {
            val intent = Intent(activity, RecipeInputActivity::class.java)
            intent.putExtra("recipe", activity.viewModel.state.value.recipe)
            val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity())
            dialog.dismiss()
            activity.editRecipeRequest.launch(intent, options)
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        binding.btnDeleteRecipe.setOnClickListener {
            ConfirmDialog { activity.viewModel.deleteRecipe() }.show(activity.supportFragmentManager, "Delete Recipe")
        }

        return dialog
    }
}