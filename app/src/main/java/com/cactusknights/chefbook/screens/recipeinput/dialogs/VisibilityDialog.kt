package com.cactusknights.chefbook.screens.recipeinput.dialogs

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
import com.cactusknights.chefbook.databinding.FragmentRecipeVisibilityBinding
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.IngredientTypes
import com.cactusknights.chefbook.models.Visibility
import com.cactusknights.chefbook.screens.recipe.dialogs.RecipeDialog
import com.cactusknights.chefbook.screens.recipeinput.RecipeInputViewModel
import com.cactusknights.chefbook.screens.recipeinput.models.RecipeInputScreenEvent
import com.cactusknights.chefbook.screens.recipeinput.models.RecipeInputScreenState


class VisibilityDialog: BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<RecipeInputViewModel>()

    private var _binding: FragmentRecipeVisibilityBinding? = null
    private val binding get() = _binding!!

    override fun getTheme() = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeVisibilityBinding.inflate(inflater, container, false)
        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvPrivate.setOnClickListener {
            viewModel.obtainEvent(RecipeInputScreenEvent.SetVisibility(Visibility.PRIVATE))
            dialog?.cancel()
        }

        binding.cvShared.setOnClickListener {
            viewModel.obtainEvent(RecipeInputScreenEvent.SetVisibility(Visibility.SHARED))
            dialog?.cancel()
        }

        binding.cvPublic.setOnClickListener {
            viewModel.obtainEvent(RecipeInputScreenEvent.SetVisibility(Visibility.PUBLIC))
            dialog?.cancel()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipeInputState.collect { state ->
                    var encrypted = false
                    if (state is RecipeInputScreenState.NewRecipe) { encrypted = state.recipeInput.isEncrypted }
                    else if (state is RecipeInputScreenState.EditRecipe) { encrypted = state.recipeInput.isEncrypted }
                    binding.cvPublic.visibility = if (!encrypted) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}