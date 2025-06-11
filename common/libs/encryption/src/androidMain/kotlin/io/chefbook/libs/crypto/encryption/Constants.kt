package io.chefbook.libs.crypto.encryption

internal const val Aes = "AES"
internal const val AesGcm = "$Aes/GCM/NoPadding"
internal const val AesGcmTagLengthBits = AesGcmTagLength * 8

internal const val PBKDF2WithHmacSHA1 = "PBKDF2withHmacSHA1"

internal const val Rsa = "RSA"
internal const val RsaEcbOaep = "$Rsa/ECB/OAEPwithSHA-256andMGF1Padding"
