package com.cactusknights.chefbook.screens.main.fragments.categories.models

import com.cactusknights.chefbook.models.Category

sealed class CategoriesEvent {
    class AddCategory(val category: Category) : CategoriesEvent()
    class UpdateCategory(val category: Category) : CategoriesEvent()
    class DeleteCategory(val category: Category) : CategoriesEvent()
}