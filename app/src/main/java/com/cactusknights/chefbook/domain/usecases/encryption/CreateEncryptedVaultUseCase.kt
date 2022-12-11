package com.cactusknights.chefbook.domain.usecases.encryption

import com.cactusknights.chefbook.common.sha1
import com.cactusknights.chefbook.core.encryption.AES_SALT_SIZE
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.Successful
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo
import com.cactusknights.chefbook.domain.interfaces.IProfileRepo
import javax.inject.Inject

const val DEFAULT_USER_ID = 1

interface ICreateEncryptedVaultUseCase {
    suspend operator fun invoke(password: String): SimpleAction
}

class CreateEncryptedVaultUseCase @Inject constructor(
    private val encryptionRepo: IEncryptedVaultRepo,
    private val profileRepo: IProfileRepo,
) : ICreateEncryptedVaultUseCase {

    override suspend operator fun invoke(password: String): SimpleAction {
        val userId = (profileRepo.getProfile() as? Successful)?.data?.id ?: DEFAULT_USER_ID
        val salt = userId.toString().sha1.toByteArray().copyOf(AES_SALT_SIZE)
        return encryptionRepo.createEncryptedVault(password, salt)
    }

}
