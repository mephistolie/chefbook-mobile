package com.cactusknights.chefbook.data.repositories.recipes

import android.graphics.BitmapFactory
import android.net.Uri
import com.cactusknights.chefbook.data.IRecipePictureSource
import com.cactusknights.chefbook.data.repositories.IFileRepo
import com.cactusknights.chefbook.data.repositories.SourceRepo
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.action.isFailure
import com.cactusknights.chefbook.domain.entities.action.isSuccess
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInput
import com.cactusknights.chefbook.domain.entities.recipe.cooking.CookingItem
import com.cactusknights.chefbook.domain.interfaces.ICryptor
import com.cactusknights.chefbook.domain.interfaces.IRecipePictureRepo
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipePictureRepo @Inject constructor(
    @Local private val localSource: IRecipePictureSource,
    @Remote private val remoteSource: IRecipePictureSource,

    private val fileSource: IFileRepo,
    private val cryptor: ICryptor,
    private val source: SourceRepo,
) : IRecipePictureRepo {

    override suspend fun syncRecipePictures(
        recipeId: Int,
        input: RecipeInput,
        key: SecretKey?
    ): RecipeInput {
        var targetSource : IRecipePictureSource = localSource
        if (source.isOnlineMode()) {
            targetSource = remoteSource
        }

        var updatedInput = input

        val result = targetSource.getPictures(recipeId)
        val uploadedPictures = if (result.isSuccess()) result.data() else emptyList()
        val usedPictures = mutableSetOf<String>()

        updatedInput.preview?.let { preview ->
            if (preview !in uploadedPictures) {
                val uploadedPreview = uploadPicture(recipeId, targetSource, preview, key)
                if (uploadedPreview != null) {
                    updatedInput = updatedInput.copy(preview = uploadedPreview)
                    usedPictures.add(uploadedPreview)
                }
            }
        }

        updatedInput.cooking.forEachIndexed { cookingIndex, item ->
            if (item is CookingItem.Step) {
                var index = 0
                var step: CookingItem.Step = item
                while (index < (step.pictures?.size ?: 0)) {
                    val picture = step.pictures?.get(index) ?: break
                    if (picture !in uploadedPictures) {
                        val uploadedPicture = uploadPicture(recipeId, targetSource, picture, key)
                        if (uploadedPicture == null) {
                            step = step.copy(pictures = step.pictures?.filterIndexed { i, _ -> i != index })
                            continue
                        }
                        step = step.copy(pictures = step.pictures?.mapIndexed { i, p -> if (i != index) p else picture })
                    }
                    usedPictures.add(step.pictures!![index])
                    updatedInput = updatedInput.copy(cooking = updatedInput.cooking.mapIndexed { i, c -> if (i != cookingIndex) c else step })
                    index++
                }
            }
        }

        deleteOutdatedPictures(recipeId, targetSource, uploadedPictures.filter { it !in usedPictures })

        return updatedInput
    }

    private suspend fun uploadPicture(recipeId: Int, source: IRecipePictureSource, path: String, key: SecretKey?) : String? {
        val getFileResult = fileSource.getFile(path)
        if (getFileResult.isFailure()) return null
        var data = getFileResult.data()

        if (key != null) {
            // Encrypt picture, if it's a decrypted
            try {
                BitmapFactory.decodeByteArray(data, 0, data.size)
                data = cryptor.encryptDataBySymmetricKey(data, key)
            } catch (e: Exception) {
                return null
            }
        }

        // Delete tmp picture
        val name = Uri.parse(path).lastPathSegment ?: return null
        localSource.deletePicture(recipeId, name)

        val result = source.addPicture(recipeId, data)

        return if (result.isSuccess()) result.data() else null
    }

    private suspend fun deleteOutdatedPictures(recipeId: Int, source: IRecipePictureSource, pictures: List<String>) {
        pictures.forEach { picturePath ->
            val uri = Uri.parse(picturePath)
            val pictureName = uri.lastPathSegment
            if (pictureName != null) source.deletePicture(recipeId, pictureName)
        }
    }

}
