package com.cactusknights.chefbook.screens.main.fragments.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.domain.usecases.CategoriesUseCases
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.screens.main.fragments.categories.models.CategoriesScreenEvent
import com.cactusknights.chefbook.screens.main.fragments.categories.models.CategoriesScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoriesUseCases: CategoriesUseCases
) : ViewModel(), EventHandler<CategoriesScreenEvent> {

    private val _categoriesState: MutableStateFlow<CategoriesScreenState> = MutableStateFlow(CategoriesScreenState.Loading)
    val categoriesState: StateFlow<CategoriesScreenState> = _categoriesState.asStateFlow()

    init {
        viewModelScope.launch {
            categoriesUseCases.listenToCategories().collect { categories ->
                if (categories != null) {
                    _categoriesState.emit(CategoriesScreenState.CategoriesUpdated(categories))
                }
            }
        }
    }

    override fun obtainEvent(event: CategoriesScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is CategoriesScreenEvent.AddCategory -> addCategory(event.category)
                is CategoriesScreenEvent.UpdateCategory -> updateCategory(event.category)
                is CategoriesScreenEvent.DeleteCategory -> deleteCategory(event.category)
            }
        }
    }

    private fun addCategory(category: Category) {
        viewModelScope.launch {
            categoriesUseCases.addCategory(category).collect { }
        }
    }

    private fun updateCategory(category: Category) {
        viewModelScope.launch {
            categoriesUseCases.updateCategory(category).collect { }
        }
    }

    private fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoriesUseCases.deleteCategory(category).collect { }
        }
    }
}