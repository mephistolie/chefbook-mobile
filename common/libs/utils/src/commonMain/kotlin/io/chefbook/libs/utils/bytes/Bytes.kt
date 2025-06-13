package io.chefbook.libs.utils.bytes

val ByteArray.hexString: String
  get() = joinToString("") { (0xFF and it.toInt()).toString(16).padStart(2, '0') }
