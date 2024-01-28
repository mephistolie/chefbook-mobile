package io.chefbook.sdk.settings.impl.data.sources.local.datastore

import android.annotation.SuppressLint
import androidx.datastore.core.Serializer
import io.chefbook.sdk.settings.impl.data.sources.local.datastore.dto.SettingsSerializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Locale

internal object SettingsSerializer : Serializer<SettingsSerializable> {

  @SuppressLint("ConstantLocale")
  override val defaultValue = SettingsSerializable(defaultRecipeLanguage = Locale.getDefault().language)

  override suspend fun readFrom(input: InputStream): SettingsSerializable {
    return try {
      Json.decodeFromString<SettingsSerializable>(input.readBytes().decodeToString())
    } catch (e: SerializationException) {
      defaultValue
    }
  }

  override suspend fun writeTo(t: SettingsSerializable, output: OutputStream) =
    output.write(Json.encodeToString(t).toByteArray())
}
