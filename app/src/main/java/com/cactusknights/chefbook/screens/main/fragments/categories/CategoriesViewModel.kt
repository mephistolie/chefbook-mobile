package com.cactusknights.chefbook.screens.main.fragments.categories

import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.base.StateViewModel
import com.cactusknights.chefbook.domain.usecases.CategoriesUseCases
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.screens.main.fragments.categories.models.CategoriesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoriesUseCases: CategoriesUseCases
) : StateViewModel<CategoriesState>(CategoriesState.Loading) {

    init {
        viewModelScope.launch {
            categoriesUseCases.listenToCategories().collect { categories ->
                _state.emit(CategoriesState.CategoriesUpdated(categories))
            }
        }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            categoriesUseCases.addCategory(category).collect { }
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            categoriesUseCases.updateCategory(category).collect { }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoriesUseCases.deleteCategory(category).collect { }
        }
    }
}