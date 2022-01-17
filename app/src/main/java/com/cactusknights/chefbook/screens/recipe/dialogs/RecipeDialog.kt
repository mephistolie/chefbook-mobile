package com.cactusknights.chefbook.screens.recipe.dialogs

import android.graphics.*
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
import com.cactusknights.chefbook.base.Constants
import com.cactusknights.chefbook.common.ConfirmDialog
import com.cactusknights.chefbook.common.ChefBookQRCodeWriter
import com.cactusknights.chefbook.common.Utils
import com.cactusknights.chefbook.databinding.FragmentRecipeMenuBinding
import com.cactusknights.chefbook.screens.recipe.RecipeViewModel
import com.cactusknights.chefbook.screens.recipe.models.RecipeActivityEvent
import com.cactusknights.chefbook.screens.recipe.models.RecipeActivityState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.collections.HashMap
import android.graphics.PorterDuff

import android.graphics.PorterDuffXfermode

import android.graphics.Shader

import android.graphics.Bitmap
import com.cactusknights.chefbook.common.ChefBookQRCodeWriter.Companion.getGradientQrCode


class RecipeDialog: BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<RecipeViewModel>()

    private var _binding: FragmentRecipeMenuBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val RECIPES_ENDPOINT = Constants.BASE_URL + "recipes/"
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
        binding.cvShare.setOnClickListener {
            val recipeState = viewModel.recipeState.value
            if (recipeState is RecipeActivityState.DataUpdated) {
                Utils.shareRecipe(recipeState.recipe, resources, ::startActivity)
            }
        }

        binding.cvEditRecipe.setOnClickListener {
            viewModel.obtainEvent(RecipeActivityEvent.EditRecipe)
            dialog?.dismiss()
        }

        binding.cvDeleteRecipe.setOnClickListener {
            ConfirmDialog { viewModel.obtainEvent(RecipeActivityEvent.DeleteRecipe) }.show(requireActivity().supportFragmentManager, "Delete Recipe")
            dialog?.dismiss()
        }


        binding.cvCategories.setOnClickListener {
            CategoriesDialog().show(requireActivity().supportFragmentManager, "Categories Dialog")
            dialog?.dismiss()
        }

//        dialog?.setOnShowListener { dialog ->
//            val recipeMenu = dialog as BottomSheetDialog
//            val bottomSheetInternal = recipeMenu.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//            if (bottomSheetInternal != null) {
//                BottomSheetBehavior.from(bottomSheetInternal).state = BottomSheetBehavior.STATE_EXPANDED
//            }
//        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipeState.collect { state ->
                    if (state is RecipeActivityState.DataUpdated) {
                        if (state.recipe.remoteId != null) {
                            binding.cvQrCode.visibility = View.VISIBLE
                            binding.textLink.visibility = View.VISIBLE
                            binding.cvLink.visibility = View.VISIBLE
                            val recipeLink = RECIPES_ENDPOINT + state.recipe.remoteId.toString()
                            binding.textLink.hint = recipeLink
                            val bitmap = getGradientQrCode(recipeLink, requireContext())
                            binding.ivQrCode.load(bitmap)
                        } else {
                            binding.cvQrCode.visibility = View.GONE
                            binding.textLink.visibility = View.GONE
                            binding.cvLink.visibility = View.GONE
                        }
                        if (state.recipe.isOwned) {
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
}