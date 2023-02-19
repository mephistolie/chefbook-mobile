package com.mysty.chefbook.api.recipe.data.crypto

import com.mysty.chefbook.api.common.crypto.ICryptor
import com.mysty.chefbook.api.recipe.data.common.dto.CookingItemSerializable
import com.mysty.chefbook.api.recipe.data.common.dto.IngredientItemSerializable
import com.mysty.chefbook.api.recipe.data.common.dto.toSerializable
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput
import com.mysty.chefbook.api.recipe.domain.entities.cooking.CookingItem
import com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState
import com.mysty.chefbook.api.recipe.domain.entities.ingredient.IngredientItem
import com.mysty.chefbook.api.sources.domain.IRecipeCryptor
import java.util.UUID
import javax.crypto.SecretKey
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.spongycastle.util.encoders.Base64

internal class RecipeCryptor(
    private val cryptor: ICryptor,
) : IRecipeCryptor {

    override fun encryptRecipe(recipe: RecipeInfo): RecipeInfo {
        with(recipe) {
            if (encryptionState !is EncryptionState.Decrypted) return this
            return copy(name = encryptData(name.toByteArray(), encryptionState.key), encryptionState = EncryptionState.Encrypted)
        }
    }

    override fun decryptRecipe(recipe: RecipeInfo, key: SecretKey): RecipeInfo {
        with(recipe) {
            if (encryptionState != EncryptionState.Encrypted) return this
            return copy(name = String(decryptData(name, key)), encryptionState = EncryptionState.Decrypted(key = key))
        }
    }

    override fun decryptRecipe(recipe: Recipe, key: SecretKey): Recipe {
        with(recipe) {
            if (encryptionState != EncryptionState.Encrypted) return this

            val ingredientsData = ingredients.getOrNull(0)
            val cookingData = cooking.getOrNull(0)

            if (ingredientsData !is IngredientItem.EncryptedData || cookingData !is CookingItem.EncryptedData) return this

            val name = String(decryptData(name, key))
            val description = description?.let { String(decryptData(description, key)) }
            val ingredients: List<IngredientItemSerializable> = Json.decodeFromString(String(decryptData(ingredientsData.content, key)))
            val cooking: List<CookingItemSerializable> = Json.decodeFromString(String(decryptData(cookingData.content, key)))

            return copy(
                name = name,
                description = description,
                ingredients = ingredients.map { it.toEntity() },
                cooking = cooking.map { it.toEntity() },
                encryptionState = EncryptionState.Decrypted(key = key)
            )
        }
    }

    override fun encryptRecipe(recipe: RecipeInput, key: SecretKey): RecipeInput {
        with(recipe) {
            val encryptedIngredients = recipe.ingredients.find { it is IngredientItem.EncryptedData }
            val encryptedCooking =  recipe.cooking.find { it is CookingItem.EncryptedData }
            if (encryptedIngredients != null || encryptedCooking != null) return copy(isEncrypted = true)

            val name = encryptData(name.toByteArray(), key)
            val description = description?.let { encryptData(it.toByteArray(), key) }
            val ingredients = encryptData(Json.encodeToString(ingredients.map { it.toSerializable() }).toByteArray(), key)
            val cooking = encryptData(Json.encodeToString(cooking.map { it.toSerializable() }).toByteArray(), key)

            return copy(
                name = name,
                description = description,
                ingredients = listOf(IngredientItem.EncryptedData(id = UUID.randomUUID().toString(), content = ingredients)),
                cooking = listOf(CookingItem.EncryptedData(id = UUID.randomUUID().toString(), content = cooking)),
                isEncrypted = true,
            )
        }
    }

    private fun decryptData(data: String, key: SecretKey) =
        cryptor.decryptDataBySymmetricKey(Base64.decode((data)), key)

    private fun encryptData(data: ByteArray, key: SecretKey) =
        Base64.toBase64String(cryptor.encryptDataBySymmetricKey(data, key))
}