package io.chefbook.features.community.recipes.ui.mvi

import io.chefbook.libs.models.language.Language
import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.tag.api.external.domain.entities.Tag

data class CommunityRecipesScreenState(
  val mode: Mode = Mode.DASHBOARD,
  val dashboard: DashboardState = DashboardState(),
  val filter: FilterState = FilterState(),
  val profileAvatar: String? = null,
  val languages: List<Language> = emptyList(),
  val recipes: List<DecryptedRecipeInfo> = emptyList(),
  val isLoading: Boolean = false,
) : MviState {

  enum class Mode {
    DASHBOARD, FILTER
  }
}

data class DashboardState(
  val tags: List<Tag> = emptyList(),
  val tab: Tab = Tab.NEW,
) {

  enum class Tab {
    NEW, VOTES, TOP, FAST
  }
}

data class FilterState(
  val search: String? = null,

  val highRatingOnly: Boolean = false,

  val minCalories: Int? = null,
  val maxCalories: Int? = null,

  val sorting: Sorting = Sorting.CREATION_TIMESTAMP,

  val languages: List<Language> = emptyList(),

  val selectedTags: List<String> = emptyList(),
  val tagGroups: List<TagGroup> = emptyList(),
) {

  val isDefault: Boolean = search.isNullOrBlank()
      && !highRatingOnly
      && sorting in Sorting.hasTab
      && minCalories == null
      && maxCalories == null
      && selectedTags.isEmpty()

  enum class Sorting {
    CREATION_TIMESTAMP, VOTES, RATING, TIME, CALORIES;

    companion object {
      val hasTab = listOf(CREATION_TIMESTAMP, VOTES, RATING, TIME)
    }
  }

  data class TagGroup(
    val name: String?,
    val tags: List<Tag>,
  )
}
