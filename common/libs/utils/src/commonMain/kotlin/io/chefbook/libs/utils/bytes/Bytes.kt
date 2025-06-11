package io.chefbook.libs.utils.bytes

val ByteArray.hexString: String
  get() = joinToString("") { (0xFF and it.toInt()).toString(16).padStart(2, '0') }

fun Int.toByteArray(): ByteArray {
  val buffer = ByteArray(4)
  for (i in 0..3) buffer[i] = (this shr (i * 8)).toByte()
  return buffer
}

fun ByteArray.toInt(): Int {
  if (this.size != 4) throw Exception("not int")
  return (this[3].toInt() shl 24) or
    (this[2].toInt() and 0xff shl 16) or
    (this[1].toInt() and 0xff shl 8) or
    (this[0].toInt() and 0xff)
}
