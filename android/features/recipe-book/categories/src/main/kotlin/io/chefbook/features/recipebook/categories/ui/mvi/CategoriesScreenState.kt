package io.chefbook.features.recipebook.categories.ui.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.tag.api.external.domain.entities.Tag

internal data class CategoriesScreenState(
    val categories: List<Collection> = emptyList(),
    val tags: List<Tag> = emptyList(),
) : MviState
