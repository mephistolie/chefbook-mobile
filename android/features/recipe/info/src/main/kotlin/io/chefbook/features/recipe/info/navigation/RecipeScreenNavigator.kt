package io.chefbook.features.recipe.info.navigation

import io.chefbook.core.android.compose.providers.ContentType
import io.chefbook.navigation.navigators.DialogNavigator

interface RecipeScreenNavigator : DialogNavigator {

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
