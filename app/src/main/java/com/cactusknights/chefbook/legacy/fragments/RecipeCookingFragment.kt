package com.cactusknights.chefbook.legacy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusknights.chefbook.legacy.adapters.CookingAdapter
import com.cactusknights.chefbook.databinding.FragmentRecipeCookingBinding
import com.cactusknights.chefbook.models.Recipe

class RecipeCookingFragment(var recipe: Recipe = Recipe()): Fragment() {

    private var cookingAdapter = CookingAdapter(recipe.cooking)

    private var _binding: FragmentRecipeCookingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeCookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textTime.text = recipe.time.toString()

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getSerializable("recipe") as Recipe
            cookingAdapter = CookingAdapter(recipe.cooking)
        }

        binding.rvSteps.layoutManager = LinearLayoutManager(context)
        binding.rvSteps.adapter = cookingAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("recipe", recipe)
        super.onSaveInstanceState(outState)
    }
}