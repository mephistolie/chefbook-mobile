package com.mysty.chefbook.api.recipe.data.repositories

import android.net.Uri
import com.mysty.chefbook.api.common.communication.Successful
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isFailure
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.common.crypto.ICryptor
import com.mysty.chefbook.api.files.data.repositories.IFilesRepo
import com.mysty.chefbook.api.recipe.domain.IRecipePictureRepo
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput
import com.mysty.chefbook.api.recipe.domain.entities.cooking.CookingItem
import com.mysty.chefbook.api.sources.domain.ISourcesRepo
import com.mysty.chefbook.core.utils.ImageUtils
import javax.crypto.SecretKey
import timber.log.Timber

internal class RecipePictureRepo(
    private val localSource: IRecipePictureSource,
    private val remoteSource: IRecipePictureSource,

    private val fileProvider: IFilesRepo,
    private val cryptor: ICryptor,
    private val source: ISourcesRepo,
) : IRecipePictureRepo {

    override suspend fun uploadRecipePictures(
        recipeId: String,
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
        recipeId: String,
        source: IRecipePictureSource,
        path: String,
        key: SecretKey?,
        isEncrypted: Boolean,
    ): String? {
        val getFileResult = fileProvider.getData(path)
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
        recipeId: String,
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

    private suspend fun deletePicture(recipeId: String, path: String) {
        val uri = Uri.parse(path)
        val pictureName = uri.lastPathSegment
        if (pictureName != null) {
            val source = if (fileProvider.isRemoteSource(path)) remoteSource else localSource
            val result = source.deletePicture(recipeId, pictureName)
            if (result.isSuccess()) Timber.i("File $path removed") else Timber.e(result.asFailure().error, "Unable to remove file $path")
        }
    }

    private fun isImageEncrypted(data: ByteArray): Boolean = !ImageUtils.isImage(data)

}

