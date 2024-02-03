package io.chefbook.sdk.auth.impl.data.sources.remote.services.password

import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.dto.ChangePasswordRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.dto.RequestPasswordResetRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.dto.ResetPasswordRequest
import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url

internal class PasswordApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), PasswordApiService {

  override suspend fun requestPasswordReset(body: RequestPasswordResetRequest): Result<MessageResponse> =
    safePost {
      url(PASSWORD_ROUTE)
      setBody(body)
    }

  override suspend fun resetPassword(body: ResetPasswordRequest): Result<MessageResponse> =
    safePatch {
      url(PASSWORD_ROUTE)
      setBody(body)
    }

  override suspend fun changePassword(body: ChangePasswordRequest): Result<MessageResponse> =
    safePut {
      url(PASSWORD_ROUTE)
      setBody(body)
    }

  companion object {
    private const val PASSWORD_ROUTE = "/v1/auth/password"
  }
}
