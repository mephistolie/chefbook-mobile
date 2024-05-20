package io.chefbook.features.community.recipes.ui.mvi

import io.chefbook.libs.mvi.MviIntent

sealed interface CommunityRecipesScreenIntent : MviIntent {

  data object Back : CommunityRecipesScreenIntent
  data object LanguagesButtonClicked : CommunityRecipesScreenIntent
  data object ProfileButtonClicked : CommunityRecipesScreenIntent

  sealed interface Content : CommunityRecipesScreenIntent {

    data object SearchButtonClicked : Content
    data object FilterButtonClicked : Content
    data class TagClicked(val tagId: String) : Content
    data object MoreTagsClicked : Content

    data class TabClicked(val tab: DashboardState.Tab) : Content

    data object CreateRecipeClicked : Content
  }

  sealed interface Filter : CommunityRecipesScreenIntent {

    data class SetSearchText(val text: String) : Filter

    data object ChangeHighRatingOnly : Filter

    data class SetMinCalories(val calories: Int?) : Filter
    data class SetMaxCalories(val calories: Int?) : Filter

    data class SetSorting(val sorting: FilterState.Sorting) : Filter

    data class TagSelected(val tagId: String) : Filter
    data class TagUnselected(val tagId: String) : Filter
    data class ExpandTagGroupClicked(val groupId: String?) : Filter
    data object TagGroupClosed : Filter

    data object ResetFilterClicked : Filter
    data object CloseFilter : Filter
    data object ConfirmFilterClicked : Filter
    data object FilterClosed : Filter
  }

  data object RecipesScrollEnded : CommunityRecipesScreenIntent

  data class RecipeCardClicked(val recipeId: String) : CommunityRecipesScreenIntent
}
