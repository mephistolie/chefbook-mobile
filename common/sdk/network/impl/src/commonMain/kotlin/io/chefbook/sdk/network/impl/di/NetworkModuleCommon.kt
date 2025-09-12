package io.chefbook.sdk.network.impl.di

import io.chefbook.libs.di.scopes.ProfileComponent
import io.chefbook.sdk.network.api.internal.clients.ProfileHttpClientFactory
import io.chefbook.sdk.network.impl.clients.ProfileHttpClientFactoryImpl
import io.chefbook.libs.di.qualifiers.HttpClient as HttpClientQualifier
import io.chefbook.sdk.network.impl.clients.plugins.EncryptedImagePlugin
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun sdkNetworkModule() = module {
  factoryOf(::EncryptedImagePlugin)

  single(named(HttpClientQualifier.BASE)) { createBaseClient() }

  single<ProfileHttpClientFactory> {
    ProfileHttpClientFactoryImpl(
      baseClient = get(named(HttpClientQualifier.BASE)),
      tokensRepository = get(),
    )
  }

  single { connectivityRepository() }

  scope<ProfileComponent> {
    scoped {
      get<ProfileHttpClientFactory>().getOrCreate(get<ProfileComponent>().profileId)
    }

    scoped(named(HttpClientQualifier.ENCRYPTED_IMAGE)) {
      val baseClient = get<HttpClient>(named(HttpClientQualifier.BASE))
      baseClient.config {
        install(
          EncryptedImagePlugin(
            encryptedVaultRepository = get(),
            recipeEncryptionRepository = get(),
          )
        )
      }
    }
  }
}
