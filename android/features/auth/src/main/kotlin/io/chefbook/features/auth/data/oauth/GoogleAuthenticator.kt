package io.chefbook.features.auth.data.oauth

import android.content.Context

interface GoogleAuthenticator {

  suspend fun signInGoogle(activityContext: Context): Result<String>

  suspend fun clearCredentialState()
}
