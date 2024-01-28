package io.chefbook.ui.common.presentation

import io.chefbook.core.android.R

enum class RecipeScreenPage(
    val titleId: Int,
) {
    INGREDIENTS(R.string.common_general_ingredients),
    COOKING(R.string.common_general_cooking);
}
