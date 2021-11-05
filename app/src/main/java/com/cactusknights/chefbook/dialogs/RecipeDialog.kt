package com.cactusknights.chefbook.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.DialogFragment
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.activities.RecipeActivity
import com.cactusknights.chefbook.activities.RecipeCommitActivity
import com.cactusknights.chefbook.databinding.DialogRecipeBinding
import com.cactusknights.chefbook.helpers.Utils
import com.cactusknights.chefbook.helpers.showToast
import com.cactusknights.chefbook.viewmodels.UuuserViewModel

class RecipeDialog(val activity: RecipeActivity): DialogFragment() {

    private lateinit var binding: DialogRecipeBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogRecipeBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(activity)
            .setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (activity.recipe.isFavourite) binding.btnFavourite.setImageResource(R.drawable.ic_favorite)
        binding.btnFavourite.setOnClickListener {
            activity.recipe.isFavourite = !activity.recipe.isFavourite
            if (activity.recipe.isFavourite) binding.btnFavourite.setImageResource(R.drawable.ic_favorite) else binding.btnFavourite.setImageResource(R.drawable.ic_unfavorite)
        }
        binding.btnShare.setOnClickListener {
            Utils.shareRecipe(activity.recipe, resources, ::startActivity)
        }
        binding.btnEditRecipe.setOnClickListener {
            val intent = Intent(activity, RecipeCommitActivity::class.java)
            intent.putExtra("targetRecipe", activity.recipe)
            intent.putExtra("allCategories", activity.allCategories)
            val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity())
            dialog.dismiss()
            activity.editRecipeRequest.launch(intent, options)
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        binding.btnDeleteRecipe.setOnClickListener {
            ConfirmDialog { UuuserViewModel.deleteRecipe(activity.recipe, ::onDeleteRecipeCallback) }.show(activity.supportFragmentManager, "Delete Recipe")
        }

        return dialog
    }

    private fun onDeleteRecipeCallback(isDeleted: Boolean) {
        if (isDeleted) {
            requireContext().showToast(resources.getString(R.string.recipe_deleted))
            dialog?.dismiss()
            requireActivity().finish()
        } else { requireContext().showToast(resources.getString(R.string.deleting_failed))}
    }
}