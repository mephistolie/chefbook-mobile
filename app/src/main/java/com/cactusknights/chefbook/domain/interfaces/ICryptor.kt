package com.cactusknights.chefbook.domain.interfaces

import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.SecretKey

interface ICryptor {
    fun generateKeyPair(): KeyPair
    fun generateSymmetricKey(): SecretKey
    fun generateSymmetricKey(passphrase: String): SecretKey

    fun encryptDataBySymmetricKey(data: ByteArray, key: SecretKey) : ByteArray
    fun decryptDataBySymmetricKey(data: ByteArray, key: SecretKey) : ByteArray

    fun encryptDataByAsymmetricKey(data: ByteArray, key: PublicKey) : ByteArray
    fun decryptDataByAsymmetricKey(data: ByteArray, key: PrivateKey) : ByteArray
}
