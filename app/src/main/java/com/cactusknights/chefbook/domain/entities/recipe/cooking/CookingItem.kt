package com.cactusknights.chefbook.domain.entities.recipe.cooking

sealed class CookingItem {

    data class Step(
        val description: String,
        val link: String? = null,
        val time: Int? = null,
        val pictures: List<String>? = null,
    ) : CookingItem()

    data class Section(
        val name: String,
    ) : CookingItem()

    data class EncryptedData(
        val content: String
    ) : CookingItem()
}
