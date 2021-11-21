package com.cactusknights.chefbook.repositories.remote.datasources

import com.cactusknights.chefbook.domain.CategoriesDataSource
import com.cactusknights.chefbook.domain.RecipesDataSource
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import com.cactusknights.chefbook.repositories.remote.dto.RecipeFavouriteInputDto
import com.cactusknights.chefbook.repositories.remote.dto.toRecipe
import com.cactusknights.chefbook.repositories.remote.dto.toRecipeInputDto
import java.io.IOException
import javax.inject.Inject

class RemoteCategoriesDataSource @Inject constructor(
    private val api: ChefBookApi
) : CategoriesDataSource {
    override suspend fun getCategories(): ArrayList<Category> {
        return api.getCategories().body()!!
    }

    override suspend fun addCategory(category: Category) : Int {
        val response =  api.addCategory(category)
        return response.body()!!.id
    }

    override suspend fun updateCategory(category: Category) {
        api.updateCategory(category.id.toString(), category)
    }

    override suspend fun deleteCategory(category: Category) {
        api.deleteCategory(category.id.toString())
    }
}