package com.mysty.chefbook.api.recipe.data.common.dto

import androidx.annotation.Keep
import com.mysty.chefbook.api.recipe.data.common.dto.CookingItemSerializable.Companion.TYPE_ENCRYPTED_DATA
import com.mysty.chefbook.api.recipe.data.common.dto.CookingItemSerializable.Companion.TYPE_SECTION
import com.mysty.chefbook.api.recipe.data.common.dto.CookingItemSerializable.Companion.TYPE_STEP
import com.mysty.chefbook.api.recipe.domain.entities.cooking.CookingItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CookingItemSerializable(
    @SerialName("id")
    val id: String,
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

    fun toEntity(): CookingItem = when (this.type.lowercase()) {
        TYPE_SECTION -> CookingItem.Section(
            id = id,
            name = text,
        )
        TYPE_ENCRYPTED_DATA -> CookingItem.EncryptedData(
            id = id,
            content = text
        )
        else -> CookingItem.Step(
            id = id,
            description = text,
            link = link,
            time = time,
            pictures = pictures.orEmpty(),
        )
    }

}

internal fun CookingItem.toSerializable(): CookingItemSerializable = when (this) {
    is CookingItem.Step -> CookingItemSerializable(
        id = id,
        text = description,
        link = link,
        time = time,
        pictures = pictures,
        type = TYPE_STEP,
    )
    is CookingItem.Section -> CookingItemSerializable(
        id = id,
        text = name,
        type = TYPE_SECTION,
    )
    is CookingItem.EncryptedData -> CookingItemSerializable(
        id = id,
        text = content,
        type = TYPE_ENCRYPTED_DATA
    )
}