package io.chefbook.features.auth.data.oauth

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import androidx.credentials.CredentialManager as AndroidCredentialManager

const val SERVER_CLIENT_ID =
  "497308718785-v8udnmjcg5l19aa8sj1cho2pcg8516qn.apps.googleusercontent.com"

class GoogleAuthenticatorImpl(
  context: Context,
) : GoogleAuthenticator {

  private val credentialManager = AndroidCredentialManager.create(context)

  override suspend fun signInGoogle(activityContext: Context): Result<String> {
    val googleIdOption = GetGoogleIdOption.Builder()
      .setFilterByAuthorizedAccounts(true)
      .setServerClientId(SERVER_CLIENT_ID)
      .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
      .addCredentialOption(googleIdOption)
      .build()

    return runCatching {
      val result = credentialManager.getCredential(request = request, context = activityContext)
      return Result.success(handleGoogleCredential(result.credential))
    }
  }

  private fun handleGoogleCredential(credential: Credential): String {
    if (credential !is CustomCredential) throw UnsupportedOperationException("invalid credential type")

    val googleIdTokenCredential = GoogleIdTokenCredential
      .createFrom(credential.data)

    return googleIdTokenCredential.idToken
  }

  override suspend fun clearCredentialState() {
    credentialManager.clearCredentialState(request = ClearCredentialStateRequest())
  }
}
