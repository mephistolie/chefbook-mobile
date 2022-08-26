package com.cactusknights.chefbook.data.dto.common.recipe

import com.cactusknights.chefbook.data.dto.common.recipe.CookingItemSerializable.Companion.TYPE_ENCRYPTED_DATA
import com.cactusknights.chefbook.data.dto.common.recipe.CookingItemSerializable.Companion.TYPE_SECTION
import com.cactusknights.chefbook.data.dto.common.recipe.CookingItemSerializable.Companion.TYPE_STEP
import com.cactusknights.chefbook.domain.entities.recipe.cooking.CookingItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CookingItemSerializable(
    @SerialName("text")
    val text: String,
    @SerialName("type")
    val type: String,
    @SerialName("link")
    val link: String? = null,
    @SerialName("time")
    val time: Int? = null,
    @SerialName("pictures")
    val pictures: List<String>? = null,
) {

    companion object {
        const val TYPE_STEP = "step"
        const val TYPE_SECTION = "section"
        const val TYPE_ENCRYPTED_DATA = "encrypted_data"
    }

}

fun CookingItemSerializable.toEntity(): CookingItem = when (this.type.lowercase()) {
    TYPE_SECTION -> CookingItem.Section(
        name = text,
    )
    TYPE_ENCRYPTED_DATA -> CookingItem.EncryptedData(
        content = text
    )
    else -> CookingItem.Step(
        description = text,
        link = link,
        time = time,
        pictures = pictures.orEmpty(),
    )
}

fun CookingItem.toSerializable(): CookingItemSerializable = when (this) {
    is CookingItem.Step -> CookingItemSerializable(
        text = description,
        link = link,
        time = time,
        pictures = pictures,
        type = TYPE_STEP,
    )
    is CookingItem.Section -> CookingItemSerializable(
        text = name,
        type = TYPE_SECTION,
    )
    is CookingItem.EncryptedData -> CookingItemSerializable(
        text = content,
        type = TYPE_ENCRYPTED_DATA
    )
}