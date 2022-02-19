package com.cactusknights.chefbook.data.repositories.recipes

import android.graphics.BitmapFactory
import android.net.Uri
import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.core.encryption.Cryptor
import com.cactusknights.chefbook.core.retrofit.UriDataProvider
import com.cactusknights.chefbook.core.retrofit.UriType
import com.cactusknights.chefbook.domain.RecipePicturesRepo
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.data.RecipePicturesDataSource
import com.cactusknights.chefbook.data.sources.local.datasources.recipes.LocalRecipePicturesDataSourceImpl
import com.cactusknights.chefbook.data.sources.remote.datasources.recipes.RemoteRecipePicturesDataSourceImpl
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import java.io.File
import java.lang.Exception
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

class RecipePicturesRepoImpl @Inject constructor(
    @Local private val localSource: RecipePicturesDataSource,
    @Remote private val remoteSource: RecipePicturesDataSource,

    private val uriProvider: UriDataProvider,
    private val cryptor: Cryptor,
    private val settings: SettingsManager,
) : RecipePicturesRepo {

    override suspend fun syncRecipePictures(recipe: DecryptedRecipe, key: SecretKey?): DecryptedRecipe {
        var targetSource : RecipePicturesDataSource = localSource
        var targetId = recipe.id!!

        val remoteId = recipe.remoteId
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE && remoteId != null){
            targetSource = remoteSource
            targetId = remoteId
        }

        val uploadedPictures = targetSource.getPicturesUri(targetId) as? ArrayList<String>?: arrayListOf()
        val usedPictures = mutableSetOf<String>()

        val preview = recipe.preview
        if (!preview.isNullOrEmpty()) {
            if (preview !in uploadedPictures) recipe.preview = uploadPicture(targetId, targetSource, preview, key)
            usedPictures.add(recipe.preview!!)
        }

        recipe.cooking.forEach { step ->
            var index = 0
            while (index < step.pictures.size) {
                val picture = step.pictures[index]
                if (picture !in uploadedPictures) {
                    val uploadedPicture = uploadPicture(targetId, targetSource, picture, key)
                    if (uploadedPicture == null) {
                        step.pictures.removeAt(index)
                        continue
                    }
                    step.pictures[index] = uploadedPicture
                }
                usedPictures.add(step.pictures[index])
                index++
            }
        }

        deleteOutdatedPictures(targetId, targetSource, uploadedPictures.filter { it !in usedPictures })
        return recipe
    }

    private suspend fun uploadPicture(recipeId: Int, source: RecipePicturesDataSource, path: String, key: SecretKey?) : String? {
        var data = uriProvider.getData(path)

        if (key != null) {
            // Encrypt picture, if it's a decrypted
            try {
                BitmapFactory.decodeByteArray(data, 0, data.size)
                data = cryptor.encryptDataBySymmetricKey(data, key)
            } catch (e: Exception) {}
        }

        // Delete tmp picture
        try {
            if (uriProvider.getUriType(path) == UriType.PATH) File(path).delete()
            else {
                val name = Uri.parse(path).lastPathSegment
                if (name != null) remoteSource.deletePicture(recipeId, name)
            }
        } catch (e: Exception) {}

        return try { source.addPicture(recipeId, data) }
        catch (e: Exception) { null }
    }

    private suspend fun deleteOutdatedPictures(recipeId: Int, source: RecipePicturesDataSource, pictures: List<String>) {
        pictures.forEach { picturePath ->
            val uri = Uri.parse(picturePath)
            val pictureName = uri.lastPathSegment
            if (pictureName != null) source.deletePicture(recipeId, pictureName)
        }
    }
}