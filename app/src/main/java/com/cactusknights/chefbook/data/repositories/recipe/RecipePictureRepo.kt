package com.cactusknights.chefbook.data.repositories.recipe

import android.net.Uri
import com.cactusknights.chefbook.core.encryption.ImageValidator
import com.cactusknights.chefbook.data.IRecipePictureSource
import com.cactusknights.chefbook.data.repositories.IFileRepo
import com.cactusknights.chefbook.data.repositories.SourceRepo
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import com.cactusknights.chefbook.domain.entities.action.Successful
import com.cactusknights.chefbook.domain.entities.action.asFailure
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
import timber.log.Timber

@Singleton
class RecipePictureRepo @Inject constructor(
    @Local private val localSource: IRecipePictureSource,
    @Remote private val remoteSource: IRecipePictureSource,

    private val fileProvider: IFileRepo,
    private val cryptor: ICryptor,
    private val source: SourceRepo,
) : IRecipePictureRepo {

    override suspend fun uploadRecipePictures(
        recipeId: Int,
        input: RecipeInput,
        key: SecretKey?,
        isEncrypted: Boolean,
        wasEncrypted: Boolean,
    ): RecipeInput {
        val targetSource = if (source.isOnlineMode()) remoteSource else localSource
        var updatedInput = input

        val uploadedPictures = (targetSource.getPictures(recipeId) as? Successful)?.data ?: emptyList()

        updatedInput.preview?.let { preview ->
            if (preview !in uploadedPictures || isEncrypted != wasEncrypted) {
                val uploadedPreview = uploadPicture(recipeId, targetSource, preview, key, isEncrypted)
                updatedInput = updatedInput.copy(preview = uploadedPreview)
            }
        }

        updatedInput.cooking.forEachIndexed { cookingIndex, item ->
            if (item is CookingItem.Step) {
                var step: CookingItem.Step = item
                step.pictures?.let { pictures ->
                    for (originalPicture in pictures) {
                        when {
                            originalPicture !in uploadedPictures || isEncrypted != wasEncrypted -> {
                                val uploadedPicture = uploadPicture(recipeId, targetSource, originalPicture, key, isEncrypted)
                                step = if (uploadedPicture != null) {
                                    step.copy(pictures = pictures.map { picture -> if (picture == originalPicture) uploadedPicture else picture })
                                } else {
                                    step.copy(pictures = pictures.filter { picture -> picture != originalPicture })
                                }
                            }
                        }
                        updatedInput =
                            updatedInput.copy(cooking = updatedInput.cooking.mapIndexed { i, c -> if (i != cookingIndex) c else step })
                    }
                }
            }
        }

        deleteOutdatedPictures(recipeId = recipeId, recipe = updatedInput, source = targetSource)

        return updatedInput
    }

    private suspend fun uploadPicture(
        recipeId: Int,
        source: IRecipePictureSource,
        path: String,
        key: SecretKey?,
        isEncrypted: Boolean,
    ): String? {
        val getFileResult = fileProvider.getFile(path)
        if (getFileResult.isFailure()) return null
        var data = getFileResult.data()

        if (key != null) {
            val wasEncrypted = isImageEncrypted(data)
            when {
                isEncrypted && !wasEncrypted -> {
                    data = cryptor.encryptDataBySymmetricKey(data, key)
                    Timber.i("Encrypting file $path...")
                }
                !isEncrypted && wasEncrypted -> {
                    data = cryptor.decryptDataBySymmetricKey(data, key)
                    Timber.i("Decrypting file $path...")
                }
            }
        }

        val result = source.addPicture(recipeId, data)
        if (result.isSuccess()) {
            Timber.i("File $path uploaded to ${result.data()}")
        } else {
            Timber.e(result.asFailure().error,"Unable to upload file $path")
        }

        deletePicture(recipeId, path)

        return if (result.isSuccess()) result.data() else null
    }

    private suspend fun deleteOutdatedPictures(
        recipeId: Int,
        recipe: RecipeInput,
        source: IRecipePictureSource,
    ) {
        val uploadedPictures = (source.getPictures(recipeId) as? Successful)?.data ?: emptyList()
        val usedPictures = listOf(
            recipe.preview,
            *recipe.cooking.filterIsInstance<CookingItem.Step>().flatMap { it.pictures ?: emptyList() }.toTypedArray()
        )

        uploadedPictures.filter { it !in usedPictures }.forEach { picturePath ->
            deletePicture(recipeId, picturePath)
        }
    }

    private suspend fun deletePicture(recipeId: Int, path: String) {
        val uri = Uri.parse(path)
        val pictureName = uri.lastPathSegment
        if (pictureName != null) {
            val source = if (fileProvider.isOnlineSource(path)) remoteSource else localSource
            val result = source.deletePicture(recipeId, pictureName)
            if (result.isSuccess()) Timber.i("File $path removed") else Timber.e(result.asFailure().error, "Unable to remove file $path")
        }
    }

    private fun isImageEncrypted(data: ByteArray): Boolean = !ImageValidator.isImage(data)

}

