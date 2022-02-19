package com.cactusknights.chefbook.common

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import com.cactusknights.chefbook.R
import java.io.*
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

fun <T : Context> T.openMail() {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:" + this.resources.getString(R.string.support_email))
    this.startActivity(intent)
}

fun <T> AsyncListDiffer<T>.forceSubmitList(list: List<T>) {
    this.submitList(list.toList())
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
        .fold("") { str, it -> str + "%02x".format(it) }
}

fun <T : Serializable> T.deepCopy(): T {
    val baos = ByteArrayOutputStream()
    val oos  = ObjectOutputStream(baos)
    oos.writeObject(this)
    oos.close()
    val bais = ByteArrayInputStream(baos.toByteArray())
    val ois  = ObjectInputStream(bais)
    @Suppress("unchecked_cast")
    return ois.readObject() as T
}