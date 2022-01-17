package com.cactusknights.chefbook.screens.recipeinput.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.FragmentRecipeInputInfoBinding
import com.cactusknights.chefbook.models.Visibility
import com.cactusknights.chefbook.screens.recipeinput.RecipeInputViewModel
import com.cactusknights.chefbook.screens.recipeinput.models.RecipeInputEvent
import com.cactusknights.chefbook.screens.recipeinput.models.RecipeInputState
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.options
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class RecipeInputInfoFragment : Fragment() {

    private val viewModel by activityViewModels<RecipeInputViewModel>()

    private var _binding: FragmentRecipeInputInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeInputInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cropImage = registerForActivityResult(CropImageContract()) {
            val uri = it.getUriFilePath(requireContext())
            if (it.isSuccessful && uri != null) {
                viewModel.obtainEvent(RecipeInputEvent.SetPreview(uri, requireContext()))
            }
        }

        binding.cvSetPreview.setOnClickListener {
            cropImage.launch(
                options {
                    setAspectRatio(5, 4)
                }
            )
        }


        binding.cvDeletePreview.setOnClickListener {
            viewModel.obtainEvent(RecipeInputEvent.DeletePreview)
        }

        binding.rgVisibility.check(binding.rbPrivate.id)
        binding.rgVisibility.setOnCheckedChangeListener { _, checkedId ->
            val visibility = when (checkedId) {
                R.id.rb_private -> Visibility.PRIVATE
                R.id.rb_shared -> Visibility.SHARED
                else -> Visibility.PUBLIC
            }
            viewModel.obtainEvent(RecipeInputEvent.SetVisibility(visibility))
        }

        binding.inputName.doOnTextChanged { name, _, _, _ -> viewModel.obtainEvent(RecipeInputEvent.InputName(name.toString())) }
        binding.inputCalories.doOnTextChanged { calories, _, _, _ -> viewModel.obtainEvent(RecipeInputEvent.InputCalories(calories.toString())) }
        binding.inputServings.doOnTextChanged { servings, _, _, _ -> viewModel.obtainEvent(RecipeInputEvent.InputServings(servings.toString())) }
        binding.inputHours.doOnTextChanged { _, _, _, _ -> viewModel.obtainEvent(RecipeInputEvent.InputTime(binding.inputHours.text.toString(), binding.inputMinutes.text.toString())) }
        binding.inputMinutes.doOnTextChanged { _, _, _, _ -> viewModel.obtainEvent(RecipeInputEvent.InputTime(binding.inputHours.text.toString(), binding.inputMinutes.text.toString())) }
        binding.inputDescription.doOnTextChanged { _, _, _, _ -> viewModel.obtainEvent(RecipeInputEvent.InputDescription(binding.inputDescription.text.toString())) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipeInputState.collect { render(it) }
            }
        }
    }

    private fun render(state: RecipeInputState) {
        if (state is RecipeInputState.NewRecipe || state is RecipeInputState.EditRecipe) {
            val recipe = if (state is RecipeInputState.NewRecipe) state.recipeInput else (state as RecipeInputState.EditRecipe).recipeInput

            if (!recipe.preview.isNullOrEmpty()) {
                if (URLUtil.isValidUrl(recipe.preview)) binding.ivPreview.load(recipe.preview!!)
                else binding.ivPreview.load(File(recipe.preview!!))

                binding.ivPreview.visibility = View.VISIBLE
                binding.cvDeletePreview.visibility = View.VISIBLE
                binding.ivAddPreview.visibility = View.GONE
            } else {
                binding.ivPreview.visibility = View.GONE
                binding.cvDeletePreview.visibility = View.GONE
                binding.ivAddPreview.visibility = View.VISIBLE
            }

            binding.inputName.setText(recipe.name)
            binding.inputServings.setText(recipe.servings.toString())

            if (state is RecipeInputState.EditRecipe) {
                binding.inputCalories.setText(recipe.calories.toString())
                if (recipe.time > 60) binding.inputHours.setText((recipe.time / 60).toString())
                if (recipe.time % 60 != 0) binding.inputMinutes.setText((recipe.time % 60).toString())
            }

            binding.rgVisibility.check(
                when (recipe.visibility) {
                    Visibility.PRIVATE -> R.id.rb_private
                    Visibility.SHARED -> R.id.rb_shared
                    else -> R.id.rb_public
                }
            )

            binding.cvMinusServing.setOnClickListener {
                if (recipe.servings > 1) {
                    binding.inputServings.setText((recipe.servings-1).toString())
                }
            }

            binding.cvPlusServing.setOnClickListener {
                if (recipe.servings < 999) {
                    binding.inputServings.setText((recipe.servings+1).toString())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}