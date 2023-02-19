package com.mysty.chefbook.api.sources.domain

import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput
import javax.crypto.SecretKey

interface IRecipeCryptor {
    fun encryptRecipe(recipe: RecipeInfo): RecipeInfo
    fun decryptRecipe(recipe: RecipeInfo, key: SecretKey): RecipeInfo
    fun decryptRecipe(recipe: Recipe, key: SecretKey): Recipe
    fun encryptRecipe(recipe: RecipeInput, key: SecretKey): RecipeInput
}
