package com.cactusknights.chefbook.screens.recipe.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.core.qr.ChefBookQRCodeWriter.Companion.getGradientQrCode
import com.cactusknights.chefbook.databinding.FragmentRecipeMenuBinding
import com.cactusknights.chefbook.screens.common.ConfirmDialog
import com.cactusknights.chefbook.screens.recipe.RecipeViewModel
import com.cactusknights.chefbook.screens.recipe.models.RecipeScreenEvent
import com.cactusknights.chefbook.screens.recipe.models.RecipeScreenState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent

import androidx.core.content.ContextCompat.getSystemService
import com.cactusknights.chefbook.common.showToast
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.IngredientTypes


class RecipeDialog: BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<RecipeViewModel>()

    private var _binding: FragmentRecipeMenuBinding? = null
    private val binding get() = _binding!!

    private var clipboard : ClipboardManager? = null

    companion object {
        const val BASE_URL = "https://chefbook.space/"
        const val RECIPES_ENDPOINT = BASE_URL + "recipes/"
    }

    override fun getTheme() = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeMenuBinding.inflate(inflater, container, false)
        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clipboard = getSystemService(requireContext(), ClipboardManager::class.java)
        binding.cvShare.setOnClickListener {
            val recipeState = viewModel.recipeState.value
            if (recipeState is RecipeScreenState.DataUpdated) {
                shareRecipe(recipeState.recipe)
            }
        }

        binding.cvEditRecipe.setOnClickListener {
            viewModel.obtainEvent(RecipeScreenEvent.EditRecipe)
            dialog?.dismiss()
        }

        binding.cvDeleteRecipe.setOnClickListener {
            ConfirmDialog { viewModel.obtainEvent(RecipeScreenEvent.DeleteRecipe) }.show(requireActivity().supportFragmentManager, "Delete Recipe")
            dialog?.dismiss()
        }


        binding.cvCategories.setOnClickListener {
            CategoriesDialog().show(requireActivity().supportFragmentManager, "Categories Dialog")
            dialog?.dismiss()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipeState.collect { state ->
                    if (state is RecipeScreenState.DataUpdated) {
                        val recipe = state.recipe
                        if (recipe.remoteId != null) {
                            binding.cvQrCode.visibility = View.VISIBLE
                            binding.textLink.visibility = View.VISIBLE
                            binding.ivCopy.visibility = View.VISIBLE
                            binding.cvLink.visibility = View.VISIBLE

                            val recipeLink = RECIPES_ENDPOINT + recipe.remoteId.toString()
                            binding.textLink.hint = recipeLink
                            binding.ivCopy.setOnClickListener {
                                val clip = ClipData.newPlainText(recipe.name, recipeLink)
                                clipboard?.setPrimaryClip(clip)
                                requireContext().showToast(R.string.link_copied)
                            }

                            val bitmap = getGradientQrCode(recipeLink, requireContext())
                            binding.ivQrCode.load(bitmap)
                        } else {
                            binding.cvQrCode.visibility = View.GONE
                            binding.textLink.visibility = View.GONE
                            binding.ivCopy.visibility = View.GONE
                            binding.cvLink.visibility = View.GONE
                        }
                        if (recipe.isOwned) {
                            binding.cvEditRecipe.visibility = View.VISIBLE
                            binding.cvDeleteRecipe.visibility = View.VISIBLE
                        } else {
                            binding.cvEditRecipe.visibility = View.GONE
                            binding.cvDeleteRecipe.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun shareRecipe(recipe: DecryptedRecipe) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        var recipeDescription = recipe.name.uppercase() + "\n\n"
        recipeDescription += resources.getString(R.string.servings_colon) + " " + recipe.servings + "\n"
        recipeDescription += resources.getString(R.string.time_colon) + " " + recipe.time + "\n"
        recipeDescription += "\n" + resources.getString(R.string.ingredients).uppercase() + ":\n"
        for (ingredient in recipe.ingredients) {
            recipeDescription += if (ingredient.type == IngredientTypes.INGREDIENT) "${ingredient.text}:\n" else "• ${ingredient.text}\n"
        }
        recipeDescription += "\n" + resources.getString(R.string.cooking) + "\n"
        for (i in recipe.cooking.indices) {
            recipeDescription += (i+1).toString() + ". " + recipe.cooking[i] + "\n"
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, recipeDescription)
        shareIntent.type = "text/html"
        startActivity(shareIntent)
    }
}