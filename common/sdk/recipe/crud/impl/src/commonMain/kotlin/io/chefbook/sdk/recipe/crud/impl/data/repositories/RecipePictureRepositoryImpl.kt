package io.chefbook.sdk.recipe.crud.impl.data.repositories

import io.chefbook.libs.encryption.HybridCryptor
import io.chefbook.libs.encryption.SymmetricKey
import io.chefbook.libs.logger.Logger
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.core.api.internal.data.models.PictureUploading
import io.chefbook.sdk.core.api.internal.data.repositories.DataSourcesRepository
import io.chefbook.sdk.file.api.internal.data.repositories.FileRepository
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.CookingItem
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Encrypted
import io.chefbook.sdk.recipe.core.api.internal.data.cache.RecipesCache
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput
import io.chefbook.sdk.recipe.crud.impl.data.models.RecipePictures
import io.chefbook.sdk.recipe.crud.impl.data.models.uploaded
import io.chefbook.sdk.recipe.crud.impl.data.sources.RecipePicturesSource
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class RecipePictureRepositoryImpl(
  private val localSource: RecipePicturesSource,
  private val remoteSource: RecipePicturesSource,

  private val cache: RecipesCache,
  private val files: FileRepository,
  private val sources: DataSourcesRepository,
) : RecipePictureRepository {

  private val mutex = Mutex()

  override suspend fun uploadRecipePictures(
    recipeId: String,
    pictures: RecipeInput.Pictures,
    key: SymmetricKey?,
  ): EmptyResult {
    val pendingPicturesCount = pictures.pendingCount
    if (pendingPicturesCount == 0) return successResult

    val uploadsResult = generateUploads(recipeId, pendingPicturesCount)
    uploadsResult.exceptionOrNull()?.let { return Result.failure(it) }

    val uploadedPictures = uploadPictures(pictures, key, uploadsResult.getOrThrow())

    return if (uploadedPictures.isNotEmpty()) {
      confirmPictures(recipeId, uploadedPictures, pictures.uploaded())
    } else {
      successResult
    }
  }

  private suspend fun generateUploads(
    recipeId: String,
    picturesCount: Int
  ): Result<List<PictureUploading>> {
    val targetSource = if (sources.isRemoteSourceEnabled()) remoteSource else localSource
    return targetSource.generatePicturesUploadLinks(recipeId, picturesCount)
  }

  private suspend fun uploadPictures(
    pictures: RecipeInput.Pictures,
    key: SymmetricKey?,
    uploads: List<PictureUploading>
  ): RecipePictures {
    var processedPictures = pictures
    var uploadIndex = 0

    suspend fun updateProcessedPictures(action: (RecipeInput.Pictures) -> RecipeInput.Pictures) =
      mutex.withLock { processedPictures = action(processedPictures) }

    fun getCurrentUpload() = uploads.getOrNull(uploadIndex).also { uploadIndex += 1 }

    supervisorScope {
      pictures.preview.ifPending { source ->
        val upload = getCurrentUpload() ?: return@ifPending

        launch {
          uploadPicture(source = source, key = key, upload = upload) { preview ->
            updateProcessedPictures { pictures -> pictures.copy(preview = preview) }
          }
        }
      }

      pictures.cooking.toList().forEach { (stepId, pictures) ->
        for (i in pictures.indices) {
          pictures[i].ifPending { source ->
            val upload = getCurrentUpload() ?: return@ifPending

            launch {
              uploadPicture(source = source, key = key, upload = upload) { picture ->
                updateProcessedPictures { processedPictures ->
                  val cooking = processedPictures.cooking.toMutableMap()
                  val stepPictures = cooking.getOrElse(stepId) { emptyList() }.toMutableList()
                  stepPictures[i] = picture
                  cooking[stepId] = stepPictures
                  processedPictures.copy(cooking = cooking)
                }
              }
            }
          }
        }
      }
    }

    return processedPictures.uploaded()
  }

  private suspend fun confirmPictures(
    recipeId: String,
    uploadedPictures: RecipePictures,
    fallbackPictures: RecipePictures,
  ): EmptyResult {
    val targetSource = if (sources.isRemoteSourceEnabled()) remoteSource else localSource
    val setPicturesResult = targetSource.setPictures(recipeId, uploadedPictures)
    val confirmedPictures = if (setPicturesResult.isSuccess) {
      if (sources.isRemoteSourceEnabled()) localSource.setPictures(recipeId, uploadedPictures)
      setPicturesResult.getOrThrow().pictures
    } else {
      fallbackPictures
    }

    cache.getRecipe(recipeId)?.let { recipe ->
      val updatedRecipe = when (recipe) {
        is Decrypted -> recipe.copy(
          info = recipe.info.copy(preview = confirmedPictures.preview),
          cooking = recipe.cooking.map {
            if (it is CookingItem.Step) {
              it.copy(pictures = confirmedPictures.cooking[it.id].orEmpty())
            } else {
              it
            }
          }
        )

        is Encrypted -> recipe.copy(
          info = recipe.info.copy(preview = confirmedPictures.preview),
          cookingPictures = confirmedPictures.cooking,
        )
      }
      cache.putRecipe(updatedRecipe)
    }

    return setPicturesResult.asEmpty()
  }

  private suspend fun uploadPicture(
    source: String,
    key: SymmetricKey? = null,
    upload: PictureUploading,
    doOnSuccess: suspend (RecipeInput.Picture.Uploaded) -> Unit,
  ): EmptyResult {
    val compressResult = files.compressImage(path = source, maxFileSize = upload.maxSize)
    val finalSource = compressResult.getOrElse { source }

    val pictureResult = files.getFile(finalSource)
      .onFailure { e ->
        Logger.e(e, "Picture not found: $finalSource")
        return Result.failure(e)
      }

    var picture = pictureResult.getOrThrow()
    if (key != null) picture = HybridCryptor.encryptDataBySymmetricKey(picture, key)

    return files.uploadFile(upload.uploadPath, picture, upload.meta)
      .onSuccess {
        doOnSuccess(RecipeInput.Picture.Uploaded(path = upload.picturePath))
        Logger.i("File $finalSource uploaded to ${upload.uploadPath}")
      }
      .onFailure { e -> Logger.e(e, "Unable to upload file $finalSource") }
  }
}

private fun RecipeInput.Picture?.ifPending(block: (String) -> Unit) =
  (this as? RecipeInput.Picture.Pending)?.let { block(it.source) }