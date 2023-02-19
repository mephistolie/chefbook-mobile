package com.mysty.chefbook.api.recipe.domain.entities.cooking

sealed class CookingItem(
    open val id: String
) {

    data class Step(
        override val id: String,
        val description: String,
        val link: String? = null,
        val time: Int? = null,
        val pictures: List<String>? = null
    ) : CookingItem(id)

    data class Section(
        override val id: String,
        val name: String
    ) : CookingItem(id)

    data class EncryptedData(
        override val id: String,
        val content: String
    ) : CookingItem(id)
}
