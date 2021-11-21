package com.cactusknights.chefbook.screens.categories

import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.common.BaseViewModel
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.usecases.CategoriesUseCases
import com.cactusknights.chefbook.models.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class CategoriesFragmentViewModel @Inject constructor(
    private val categoriesUseCases: CategoriesUseCases
) : BaseViewModel<CategoriesListState>(CategoriesListState()) {

    init {
        viewModelScope.launch {
            getCategories()
        }
    }

    private suspend fun getCategories() {
        categoriesUseCases.getCategoriesList().collect { result ->
            when (result) {
                is Result.Success -> {
                    updateCategories(result.data!!)
                }
                else -> {
                }
            }
        }
    }

    private suspend fun addCategory(category: Category) {
        categoriesUseCases.addCategory(category).collect { result ->
            when (result) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    state.value.categories.add(result.data!!)
                    _state.emit(
                        CategoriesListState(
                            categories = state.value.categories
                        )
                    )
                }
                is Result.Error -> {
                }
            }
        }
    }

    private suspend fun updateCategory(category: Category) {
        categoriesUseCases.addCategory(category).collect { result ->
            when (result) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    _state.emit(
                        CategoriesListState(
                            categories = state.value.categories.map { if (it.id == category.id) category else it } as ArrayList<Category>
                        )
                    )
                }
                is Result.Error -> {
                }
            }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoriesUseCases.deleteCategory(category).collect { result ->
                when (result) {
                    is Result.Loading -> {
                    }
                    is Result.Success -> {
                        _state.emit(
                            CategoriesListState(
                                categories = state.value.categories.filter { it.id != category.id } as ArrayList<Category>
                            )
                        )
                    }
                    is Result.Error -> {
                    }
                }
            }
        }
    }

    private suspend fun updateCategories(categories: ArrayList<Category>) {
        categories.sortBy { it.name }
        _state.emit(
            CategoriesListState(
                categories = categories
            )
        )
    }
}