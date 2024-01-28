package io.chefbook.libs.utils.hash

private const val MD5 = "MD5"
private const val SHA1 = "SHA-1"
private const val SHA256 = "SHA-256"

val String.md5: String get() = hashString(this, MD5)
val String.sha1: String get() = hashString(this, SHA1)
val String.sha256: String get() = hashString(this, SHA256)

fun Int.toByteArray() : ByteArray {
    val buffer = ByteArray(4)
    for (i in 0..3) buffer[i] = (this shr (i*8)).toByte()
    return buffer
}

fun ByteArray.toInt() : Int {
    if (this.size != 4) throw Exception("not int")
    return (this[3].toInt() shl 24) or
      (this[2].toInt() and 0xff shl 16) or
      (this[1].toInt() and 0xff shl 8) or
      (this[0].toInt() and 0xff)
}