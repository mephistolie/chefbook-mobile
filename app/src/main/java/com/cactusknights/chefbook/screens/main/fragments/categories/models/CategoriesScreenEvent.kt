package com.cactusknights.chefbook.screens.main.fragments.categories.models

import com.cactusknights.chefbook.models.Category

sealed class CategoriesScreenEvent {
    class AddCategory(val category: Category) : CategoriesScreenEvent()
    class UpdateCategory(val category: Category) : CategoriesScreenEvent()
    class DeleteCategory(val category: Category) : CategoriesScreenEvent()
}