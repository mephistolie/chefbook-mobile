package io.chefbook.libs.crypto.encryption

import io.chefbook.libs.utils.interop.bridgingRelease
import io.chefbook.libs.utils.interop.bridgingRetain
import io.chefbook.libs.utils.interop.descriptionReleasing
import io.chefbook.libs.utils.interop.mutableDictOf
import io.chefbook.libs.utils.interop.withBridgingRelease
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.CoreFoundation.CFDataRef
import platform.CoreFoundation.CFErrorRefVar
import platform.Foundation.NSData
import platform.Foundation.NSNumber
import platform.Security.SecKeyCreateRandomKey
import platform.Security.SecKeyCreateWithData
import platform.Security.SecKeyCopyExternalRepresentation
import platform.Security.SecKeyCopyPublicKey
import platform.Security.SecKeyCreateDecryptedData
import platform.Security.SecKeyCreateEncryptedData
import platform.Security.kSecAttrKeySizeInBits
import platform.Security.kSecAttrKeyType
import platform.Security.kSecAttrKeyTypeRSA
import platform.Security.kSecAttrKeyClass
import platform.Security.kSecAttrKeyClassPublic
import platform.Security.kSecAttrKeyClassPrivate
import platform.Security.kSecKeyAlgorithmRSAEncryptionOAEPSHA256

object ССRSA {

  private val rsaKeyParameters
    get() = mutableDictOf(
      kSecAttrKeyType to kSecAttrKeyTypeRSA,
      kSecAttrKeySizeInBits to NSNumber(RsaKeySize).bridgingRetain(),
    )

  private val rsaPublicKeyAttributes
    get() = mutableDictOf(
      kSecAttrKeyType to kSecAttrKeyTypeRSA,
      kSecAttrKeyClass to kSecAttrKeyClassPublic,
      kSecAttrKeySizeInBits to NSNumber(RsaKeySize).bridgingRetain(),
    )

  private val rsaPrivateKeyAttributes
    get() = mutableDictOf(
      kSecAttrKeyType to kSecAttrKeyTypeRSA,
      kSecAttrKeyClass to kSecAttrKeyClassPrivate,
      kSecAttrKeySizeInBits to NSNumber(RsaKeySize).bridgingRetain(),
    )

  fun generateKey(): Pair<NSData, NSData> = rsaKeyParameters.withBridgingRelease { parameters ->
    memScoped {
      val error = alloc<CFErrorRefVar>()
      val privateKey = SecKeyCreateRandomKey(
        parameters = parameters,
        error = error.ptr,
      ) ?: error("Failed to generate key pair: ${error.descriptionReleasing}")

      val privateKeyRaw = SecKeyCopyExternalRepresentation(
        key = privateKey,
        error = error.ptr
      )?.nSData
        ?: error("Failed to get private key raw representation: ${error.descriptionReleasing}")

      val publicKey = SecKeyCopyPublicKey(privateKey)
        ?: error("Failed to get extract public key from private: ${error.descriptionReleasing}")

      val publicKeyRaw = SecKeyCopyExternalRepresentation(
        key = publicKey,
        error = error.ptr
      )?.nSData
        ?: error("Failed to get public key raw representation: ${error.descriptionReleasing}")

      publicKeyRaw to privateKeyRaw
    }
  }

  fun getPublicKey(
    privateKey: NSData,
  ): NSData = rsaPrivateKeyAttributes.withBridgingRelease { attributes ->
    memScoped {
      val error = alloc<CFErrorRefVar>()

      val privateSecKey = SecKeyCreateWithData(
        keyData = privateKey.cFData,
        attributes = attributes,
        error = error.ptr,
      ) ?: error("Failed to convert byte array to private key: ${error.descriptionReleasing}")

      val publicKey = SecKeyCopyPublicKey(privateSecKey)
        ?: error("Failed to get extract public key from private: ${error.descriptionReleasing}")

      val publicKeyRaw = SecKeyCopyExternalRepresentation(
        key = publicKey,
        error = error.ptr
      )?.nSData
        ?: error("Failed to get public key raw representation: ${error.descriptionReleasing}")

      publicKeyRaw
    }
  }

  fun encryptWithPublicKey(
    publicKey: NSData,
    plaintext: NSData,
  ): NSData = rsaPublicKeyAttributes.withBridgingRelease { attributes ->
    memScoped {
      val error = alloc<CFErrorRefVar>()

      val publicSecKey = SecKeyCreateWithData(
        keyData = publicKey.cFData,
        attributes = attributes,
        error = error.ptr,
      ) ?: error("Failed to convert byte array to public key: ${error.descriptionReleasing}")

      val ciphertext = SecKeyCreateEncryptedData(
        key = publicSecKey,
        algorithm = kSecKeyAlgorithmRSAEncryptionOAEPSHA256,
        plaintext = plaintext.cFData,
        error = error.ptr,
      )?.nSData ?: error("Failed to encrypt plaintext with key: ${error.descriptionReleasing}")

      ciphertext
    }
  }

  fun decryptWithPrivateKey(
    privateKey: NSData,
    ciphertext: NSData,
  ): NSData = rsaPrivateKeyAttributes.withBridgingRelease { attributes ->
    memScoped {
      val error = alloc<CFErrorRefVar>()

      val privateSecKey = SecKeyCreateWithData(
        keyData = privateKey.cFData,
        attributes = attributes,
        error = error.ptr,
      ) ?: error("Failed to convert byte array to private key: ${error.descriptionReleasing}")

      val plaintext = SecKeyCreateDecryptedData(
        key = privateSecKey,
        algorithm = kSecKeyAlgorithmRSAEncryptionOAEPSHA256,
        ciphertext = ciphertext.cFData,
        error = error.ptr,
      )?.nSData ?: error("Failed to decrypt ciphertext with key: ${error.descriptionReleasing}")

      plaintext
    }
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  private val CFDataRef.nSData: NSData?
    get() = bridgingRelease<NSData>()

  @Suppress("CAST_NEVER_SUCCEEDS")
  private val NSData.cFData: CFDataRef?
    get() = bridgingRetain<CFDataRef>()
}
