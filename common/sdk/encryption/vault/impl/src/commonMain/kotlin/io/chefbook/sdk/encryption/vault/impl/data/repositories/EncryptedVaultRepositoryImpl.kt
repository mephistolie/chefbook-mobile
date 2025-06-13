package io.chefbook.sdk.encryption.vault.impl.data.repositories

import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.crypto.encryption.models.AsymmetricKey
import io.chefbook.libs.crypto.encryption.models.AsymmetricPrivateKey
import io.chefbook.libs.crypto.encryption.models.AsymmetricPublicKey
import io.chefbook.libs.crypto.encryption.HybridCryptor
import io.chefbook.libs.crypto.encryption.encryptPrivateKeyBySymmetricKey
import io.chefbook.libs.crypto.encryption.generateIV
import io.chefbook.libs.crypto.encryption.models.SymmetricCipherData
import io.chefbook.libs.exceptions.ServerException
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.core.api.internal.data.repositories.DataSourcesRepository
import io.chefbook.sdk.encryption.vault.api.external.domain.entities.EncryptedVaultState
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository
import io.chefbook.sdk.encryption.vault.impl.data.sources.local.LocalEncryptedVaultSource
import io.chefbook.sdk.encryption.vault.impl.data.sources.models.EncryptedVaultKey
import io.chefbook.sdk.encryption.vault.impl.data.sources.remote.RemoteEncryptedVaultSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class EncryptedVaultRepositoryImpl(
  private val profileId: String,
  private val localSource: LocalEncryptedVaultSource,
  private val remoteSource: RemoteEncryptedVaultSource,

  private val sources: DataSourcesRepository,
  private val dispatchers: AppDispatchers,
  profileScope: CoroutineScope,
) : EncryptedVaultRepository {

  private val vaultState: MutableStateFlow<InternalEncryptionState> =
    MutableStateFlow(InternalEncryptionState.Loading)

  private val initEncryptionStateJob = profileScope.launch {
    val localResult = localSource.getEncryptedVaultKey()
    val state = localResult.getOrNull()?.let(InternalEncryptionState::Locked)
      ?: InternalEncryptionState.Disabled
    vaultState.emit(state)
  }

  private var remoteResponseHandledBefore: Boolean = false

  override fun observeEncryptedVaultState(): Flow<EncryptedVaultState> = vaultState
    .asStateFlow()
    .map(InternalEncryptionState::asEncryptionState)

  override suspend fun getEncryptedVaultState() =
    getInternalEncryptedVaultState().asEncryptionState()

  private suspend fun getInternalEncryptedVaultState(): InternalEncryptionState {
    (vaultState.value as? InternalEncryptionState.Unlocked)?.let { return it }
    val result = getEncryptedPrivateKey()

    return if (result.isSuccess) InternalEncryptionState.Locked(result.getOrThrow()) else InternalEncryptionState.Disabled
  }

  override suspend fun refreshEncryptedVaultState(isEnabled: Boolean) =
    withContext(dispatchers.io) {
      initEncryptionStateJob.join()

      return@withContext when {
        isEnabled && vaultState.value == InternalEncryptionState.Disabled -> getEncryptedPrivateKey().asEmpty()
        !isEnabled && vaultState.value != InternalEncryptionState.Disabled -> {
          vaultState.tryEmit(InternalEncryptionState.Disabled)
          localSource.deleteEncryptedVault()
        }

        else -> successResult
      }
    }

  override suspend fun createEncryptedVault(password: String) =
    withContext(dispatchers.computation) {
      initEncryptionStateJob.join()
      if (vaultState.value != InternalEncryptionState.Disabled) {
        return@withContext Result.failure(UnsupportedOperationException("Encrypted vault already exists"))
      }

      val passwordKeySalt = HybridCryptor.generateSalt()
      val passwordKey = HybridCryptor.generatePasswordSymmetricKey(
        password = password,
        salt = passwordKeySalt,
      )

      val vaultKeyPair = HybridCryptor.generateAsymmetricKey()
      val encryptedVaultKeyPrivateKey = HybridCryptor.encryptPrivateKeyBySymmetricKey(
        privateKey = vaultKeyPair.private,
        symmetricKey = passwordKey,
        iv = HybridCryptor.generateIV(seed = profileId),
      ).combinedData

      val result = if (sources.isRemoteSourceEnabled()) {
        remoteSource.createEncryptedVault(
          publicKey = vaultKeyPair.public,
          privateKey = encryptedVaultKeyPrivateKey,
          passwordSalt = passwordKeySalt,
        ).onSuccess {
          localSource.setEncryptedVaultKey(
            encryptedPrivateKey = encryptedVaultKeyPrivateKey,
            passwordSalt = passwordKeySalt,
          )
        }
      } else {
        localSource.setEncryptedVaultKey(
          encryptedPrivateKey = encryptedVaultKeyPrivateKey,
          passwordSalt = passwordKeySalt,
        )
      }

      result.onSuccess { vaultState.emit(InternalEncryptionState.Unlocked(vaultKeyPair)) }
    }

  override suspend fun unlockEncryptedVault(password: String) =
    withContext(dispatchers.computation) {
      val encryptedvaultKeyResult = getEncryptedPrivateKey()
      if (encryptedvaultKeyResult.isFailure) return@withContext encryptedvaultKeyResult.asEmpty()

      val key = HybridCryptor.generatePasswordSymmetricKey(
        password = password,
        salt = encryptedvaultKeyResult.getOrThrow().passwordSalt,
      )

      return@withContext try {
        val vaultKeys =
          HybridCryptor.decryptAsymmetricKeyBySymmetricKey(
            cipherData = SymmetricCipherData(
              combinedData = encryptedvaultKeyResult.getOrThrow().key,
              iv = HybridCryptor.generateIV(seed = profileId),
            ),
            key = key,
          )
        vaultState.emit(InternalEncryptionState.Unlocked(vaultKeys))
        successResult
      } catch (e: Exception) {
        Result.failure(e)
      }
    }

  override suspend fun lockEncryptedVault(): EmptyResult {
    return getEncryptedPrivateKey()
      .onSuccess { encryptedKey ->
        vaultState.emit(InternalEncryptionState.Locked(encryptedKey))
      }
      .asEmpty()
  }

  override suspend fun getVaultPublicKey(): Result<AsymmetricPublicKey> =
    (vaultState.value as? InternalEncryptionState.Unlocked)?.let { state -> Result.success(state.key.public) }
      ?: Result.failure(Exception("vault isn't unlocked"))

  override suspend fun getVaultPrivateKey(): Result<AsymmetricPrivateKey> =
    (vaultState.value as? InternalEncryptionState.Unlocked)?.let { state -> Result.success(state.key.private) }
      ?: Result.failure(Exception("vault isn't unlocked"))

  override suspend fun deleteEncryptedVault(): EmptyResult {
    val result = if (sources.isRemoteSourceEnabled()) {
      remoteSource.deleteEncryptedVault()
        .onSuccess { localSource.deleteEncryptedVault() }
    } else {
      localSource.deleteEncryptedVault()
    }

    return result.onSuccess { vaultState.emit(InternalEncryptionState.Disabled) }
  }

  private suspend fun getEncryptedPrivateKey(): Result<EncryptedVaultKey> {
    (vaultState.value as? InternalEncryptionState.Locked)?.let { return Result.success(it.encryptedKey) }

    val localResult = localSource.getEncryptedVaultKey()
    if (remoteResponseHandledBefore || !sources.isRemoteSourceAvailable()) return localResult

    val remoteResult = remoteSource.getEncryptedVaultKey()
      .onSuccess { encryptedKey ->
        if (localResult.isFailure) {
          localSource.setEncryptedVaultKey(
            encryptedPrivateKey = encryptedKey.key,
            passwordSalt = encryptedKey.passwordSalt,
          )
        }
        vaultState.emit(InternalEncryptionState.Locked(encryptedKey))
        remoteResponseHandledBefore = true
      }
      .onFailure { e ->
        if ((e as? ServerException)?.type == ServerException.NOT_FOUND) {
          localSource.deleteEncryptedVault()
          vaultState.emit(InternalEncryptionState.Disabled)
          remoteResponseHandledBefore = true
        }
      }
    return when {
      remoteResult.isSuccess -> remoteResult
      ((remoteResult.exceptionOrNull() as? ServerException)?.type == ServerException.NOT_FOUND) -> remoteResult
      else -> {
        localResult
      }
    }
  }

  override suspend fun clearLocalData(): EmptyResult {
    val localResult = localSource.deleteEncryptedVault()
    vaultState.emit(InternalEncryptionState.Loading)

    return localResult
  }

  private sealed interface InternalEncryptionState {

    data object Loading : InternalEncryptionState

    data object Disabled : InternalEncryptionState

    class Locked(val encryptedKey: EncryptedVaultKey) : InternalEncryptionState

    data class Unlocked(val key: AsymmetricKey) : InternalEncryptionState

    fun asEncryptionState() =
      when (this) {
        is Loading -> EncryptedVaultState.LOADING
        is Disabled -> EncryptedVaultState.DISABLED
        is Locked -> EncryptedVaultState.LOCKED
        is Unlocked -> EncryptedVaultState.UNLOCKED
      }
  }
}
