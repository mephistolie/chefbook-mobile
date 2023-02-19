package com.mysty.chefbook.core.utils

object ImageUtils {

    private val jpegHeader = byteArrayOf(
        0xFF.toByte(),
        0xD8.toByte(),
        0xFF.toByte(),
    )

    private val pngHeader = byteArrayOf(
        0x89.toByte(),
        'P'.code.toByte(),
        'N'.code.toByte(),
        'G'.code.toByte(),
        0x0D.toByte(),
        0x0A.toByte(),
        0x1A.toByte(),
        0x0A.toByte()
    )

    private val bmpHeader = byteArrayOf(
        'B'.code.toByte(),
        'M'.code.toByte(),
    )

    val MAX_IMAGE_HEADER_LENGTH = maxOf(jpegHeader.size, pngHeader.size, bmpHeader.size)

    fun isImage(data: ByteArray): Boolean {
        return isJpeg(data) || isPng(data) || isBmp(data)
    }

    private fun isJpeg(data: ByteArray) = startsWithPattern(data, jpegHeader)

    private fun isPng(data: ByteArray) = startsWithPattern(data, pngHeader)

    private fun isBmp(data: ByteArray) = startsWithPattern(data, bmpHeader)

    private fun startsWithPattern(data: ByteArray, pattern: ByteArray): Boolean {
        if (pattern.size > data.size) return false
        for (index in pattern.indices) {
            if (pattern[index] != data[index]) return false
        }
        return true
    }
}
