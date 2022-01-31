package com.cactusknights.chefbook.common

import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest

private const val MD5 = "MD5"
private const val SHA1 = "SHA-1"
private const val SHA256 = "SHA-256"

fun <T : Context> T.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun <T : Context> T.showToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toByteArray() : ByteArray {
    val buffer = ByteArray(4)
    for (i in 0..3) buffer[i] = (this shr (i*8)).toByte()
    return buffer
}

fun ByteArray.toInt() : Int {
    if (this.size != 4) throw IOException()
    return (this[3].toInt() shl 24) or
            (this[2].toInt() and 0xff shl 16) or
            (this[1].toInt() and 0xff shl 8) or
            (this[0].toInt() and 0xff)
}

val String.md5: String get() = hashString(this, MD5)
val String.sha1: String get() = hashString(this, SHA1)
val String.sha256: String get() = hashString(this, SHA256)

private fun hashString(input: String, algorithm: String): String {
    return MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
        .fold("", { str, it -> str + "%02x".format(it) })
}