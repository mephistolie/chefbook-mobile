package com.cactusknights.chefbook.core.qr

/*
 * Copyright 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat
import com.cactusknights.chefbook.R
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.qrcode.encoder.ByteMatrix
import com.google.zxing.qrcode.encoder.Encoder
import java.io.IOException
import java.util.*
import kotlin.math.roundToInt

/**
 * This object renders a QR Code as a BitMatrix 2D array of greyscale values.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */

/**
 * Ported from Java to Kotlin and changed for app purposes by Mikhail Levin
 */

class ChefBookQRCodeWriter {
    private var input: ByteMatrix? = null
    private val radii = FloatArray(8)
    private var imageBlocks = 0
    private var imageBlockX = 0
    private var sideQuadSize = 0
    private val errorCorrectionLevel = ErrorCorrectionLevel.M
    private val radiusFactor = 0.75f

    fun encode(
        contents: String,
        imSize: Int,
        bm: Bitmap?,
        backgroundColor: Int,
        color: Int,
        context: Context
    ): Bitmap {
        var bitmap = bm
        if (contents.isEmpty() || imSize < 0) throw IOException()

        val code = Encoder.encode(contents, errorCorrectionLevel)
        input = code.matrix
        checkNotNull(input)
        val inputWidth = input!!.width
        val inputHeight = input!!.height
        for (x in 0 until inputWidth) {
            if (has(x, 0)) {
                sideQuadSize++
            } else {
                break
            }
        }
        val qrWidth = inputWidth + QUIET_ZONE_SIZE * 2
        val qrHeight = inputHeight + QUIET_ZONE_SIZE * 2
        val outputWidth = maxOf(imSize, qrWidth)
        val outputHeight = maxOf(imSize, qrHeight)
        val multiple = minOf(outputWidth / qrWidth, outputHeight / qrHeight)
        val padding = 16
        val size = multiple * inputWidth + padding * 2
        if (bitmap == null || bitmap.width != size) {
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(bitmap!!)
        canvas.drawColor(backgroundColor)
        val blackPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        blackPaint.color = color
        val rect = GradientDrawable()
        rect.shape = GradientDrawable.RECTANGLE
        rect.cornerRadii = radii
        imageBlocks = ((size - 32) / 4.65f / multiple).roundToInt()
        if (imageBlocks % 2 != inputWidth % 2) { imageBlocks++ }
        imageBlockX = (inputWidth - imageBlocks) / 2
        val imageSize = imageBlocks * multiple - 24
        val imageX = (size - imageSize*1.25) / 2
        val isTransparentBackground = Color.alpha(backgroundColor) == 0
        val clipPath = Path()
        val rectF = RectF()
        for (a in 0..2) {
            var x: Int
            var y: Int
            when (a) {
                0 -> {
                    x = padding
                    y = padding
                }
                1 -> {
                    x = size - sideQuadSize * multiple - padding
                    y = padding
                }
                else -> {
                    x = padding
                    y = size - sideQuadSize * multiple - padding
                }
            }
            var r: Float
            if (isTransparentBackground) {
                rectF[(x + multiple).toFloat(), (y + multiple).toFloat(), (x + (sideQuadSize - 1) * multiple).toFloat()] =
                    (y + (sideQuadSize - 1) * multiple).toFloat()
                r = sideQuadSize * multiple / 4.0f * radiusFactor
                clipPath.reset()
                clipPath.addRoundRect(rectF, r, r, Path.Direction.CW)
                clipPath.close()
                canvas.save()
                canvas.clipPath(clipPath, Region.Op.DIFFERENCE)
            }
            r = sideQuadSize * multiple / 3.0f * radiusFactor
            Arrays.fill(radii, r)
            rect.setColor(color)
            rect.setBounds(x, y, x + sideQuadSize * multiple, y + sideQuadSize * multiple)
            rect.draw(canvas)
            canvas.drawRect(
                (x + multiple).toFloat(),
                (y + multiple).toFloat(),
                (x + (sideQuadSize - 1) * multiple).toFloat(),
                (y + (sideQuadSize - 1) * multiple).toFloat(),
                blackPaint
            )
            if (isTransparentBackground) {
                canvas.restore()
            }
            if (!isTransparentBackground) {
                r = sideQuadSize * multiple / 4.0f * radiusFactor
                Arrays.fill(radii, r)
                rect.setColor(backgroundColor)
                rect.setBounds(
                    x + multiple,
                    y + multiple,
                    x + (sideQuadSize - 1) * multiple,
                    y + (sideQuadSize - 1) * multiple
                )
                rect.draw(canvas)
            }
            r = (sideQuadSize - 2) * multiple / 4.0f * radiusFactor
            Arrays.fill(radii, r)
            rect.setColor(color)
            rect.setBounds(
                x + multiple * 2,
                y + multiple * 2,
                x + (sideQuadSize - 2) * multiple,
                y + (sideQuadSize - 2) * multiple
            )
            rect.draw(canvas)
        }
        val r = multiple / 2.0f * radiusFactor
        var y = 0
        var outputY = padding
        while (y < inputHeight) {
            var x = 0
            var outputX = padding
            while (x < inputWidth) {
                if (has(x, y)) {
                    Arrays.fill(radii, r)
                    if (has(x, y - 1)) {
                        radii[1] = 0F
                        radii[0] = radii[1]
                        radii[3] = 0F
                        radii[2] = radii[3]
                    }
                    if (has(x, y + 1)) {
                        radii[7] = 0F
                        radii[6] = radii[7]
                        radii[5] = 0F
                        radii[4] = radii[5]
                    }
                    if (has(x - 1, y)) {
                        radii[1] = 0F
                        radii[0] = radii[1]
                        radii[7] = 0F
                        radii[6] = radii[7]
                    }
                    if (has(x + 1, y)) {
                        radii[3] = 0F
                        radii[2] = radii[3]
                        radii[5] = 0F
                        radii[4] = radii[5]
                    }
                    rect.setColor(color)
                    rect.setBounds(outputX, outputY, outputX + multiple, outputY + multiple)
                    rect.draw(canvas)
                } else {
                    var has = false
                    Arrays.fill(radii, 0f)
                    if (has(x - 1, y - 1) && has(x - 1, y) && has(x, y - 1)) {
                        radii[1] = r
                        radii[0] = radii[1]
                        has = true
                    }
                    if (has(x + 1, y - 1) && has(x + 1, y) && has(x, y - 1)) {
                        radii[3] = r
                        radii[2] = radii[3]
                        has = true
                    }
                    if (has(x - 1, y + 1) && has(x - 1, y) && has(x, y + 1)) {
                        radii[7] = r
                        radii[6] = radii[7]
                        has = true
                    }
                    if (has(x + 1, y + 1) && has(x + 1, y) && has(x, y + 1)) {
                        radii[5] = r
                        radii[4] = radii[5]
                        has = true
                    }
                    if (has && !isTransparentBackground) {
                        canvas.drawRect(
                            outputX.toFloat(),
                            outputY.toFloat(),
                            (outputX + multiple).toFloat(),
                            (outputY + multiple).toFloat(),
                            blackPaint
                        )
                        rect.setColor(backgroundColor)
                        rect.setBounds(outputX, outputY, outputX + multiple, outputY + multiple)
                        rect.draw(canvas)
                    }
                }
                x++
                outputX += multiple
            }
            y++
            outputY += multiple
        }
        val icon = getBitmap(
            ContextCompat.getDrawable(context, R.drawable.ic_broccy_monochrome) as VectorDrawable?,
            imageSize
        )
        canvas.drawBitmap(icon, imageX.toFloat(), imageX.toFloat(), null)
        icon.recycle()
        canvas.setBitmap(null)
        return bitmap
    }

    private fun has(x: Int, y: Int): Boolean {
        if (x >= imageBlockX && x < imageBlockX + imageBlocks && y >= imageBlockX && y < imageBlockX + imageBlocks) { return false }
        if ((x < sideQuadSize || x >= input!!.width - sideQuadSize) && y < sideQuadSize) { return false }
        return if (x < sideQuadSize && y >= input!!.height - sideQuadSize) { false }
        else x >= 0 && y >= 0 && x < input!!.width && y < input!!.height && input!![x, y].toInt() == 1
    }

    companion object {
        private const val QUIET_ZONE_SIZE = 4

        fun getGradientQrCode(data: String, context: Context) : Bitmap? {
            val qrBitmap: Bitmap?
            val qrBitmapSize = 1080
            val hints = HashMap<EncodeHintType, Any>()
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.M
            val writer = ChefBookQRCodeWriter()
            EncodeHintType.MARGIN
            qrBitmap = writer.encode(data, qrBitmapSize, null, ContextCompat.getColor(context, R.color.white_transparent), ContextCompat.getColor(context, R.color.monochrome_invert), context)
            return addGradient(qrBitmap)
        }

        private fun addGradient(originalBitmap: Bitmap) : Bitmap {
            val width: Int = originalBitmap.width
            val height: Int = originalBitmap.height
            val updatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(updatedBitmap)

            canvas.drawBitmap(originalBitmap, 0f, 0f, null)

            val paint = Paint()
            val shader = LinearGradient(width.toFloat(), 0f, 0f, height.toFloat(), Color.parseColor("#bf6639"), Color.parseColor("#120426"), Shader.TileMode.CLAMP)
            paint.shader = shader
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

            return updatedBitmap
        }

        private fun getBitmap(vectorDrawable: VectorDrawable?, imageSize: Int): Bitmap {
            val bitmap = Bitmap.createBitmap((imageSize*1.25).roundToInt(), (imageSize*1.25).roundToInt(), Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            vectorDrawable!!.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)
            return bitmap
        }
    }
}