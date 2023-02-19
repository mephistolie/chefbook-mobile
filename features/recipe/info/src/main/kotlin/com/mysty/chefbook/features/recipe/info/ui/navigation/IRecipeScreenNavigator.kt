package com.mysty.chefbook.features.recipe.info.ui.navigation

import com.mysty.chefbook.core.android.compose.providers.ContentType
import com.mysty.chefbook.navigation.navigators.IDialogNavigator

interface IRecipeScreenNavigator : IDialogNavigator {
    fun openRecipeInputScreen(recipeId: String? = null)
    fun openCategoryRecipesScreen(categoryId: String)
    fun openRecipeShareDialog(recipeId: String)
    fun openPicturesViewer(
        pictures: Array<String>,
        startIndex: Int = 0,
        picturesType: ContentType = ContentType.DECRYPTED,
    )
    fun closeRecipeScreen()
}
