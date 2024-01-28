package io.chefbook.sdk.auth.impl.data.sources.remote.services.password

import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.dto.ChangePasswordRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.dto.RequestPasswordResetRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.dto.ResetPasswordRequest
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse

internal interface PasswordApiService {
  suspend fun requestPasswordReset(body: RequestPasswordResetRequest): Result<MessageResponse>
  suspend fun resetPassword(body: ResetPasswordRequest): Result<MessageResponse>
  suspend fun changePassword(body: ChangePasswordRequest): Result<MessageResponse>
}
