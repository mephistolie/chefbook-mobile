package io.chefbook.libs.utils.numbers

import io.chefbook.libs.logger.Logger
import kotlin.math.abs

fun String.toFormattedFloat(decimalCount: Int = 3): Float? {
  val formattedInput = replace(",", ".")
  val dotIndex = formattedInput.indexOfLast { ch -> ch == '.' }
  var decimalPlaces = 0
  if (dotIndex > -1) decimalPlaces = formattedInput.lastIndex - dotIndex
  return formattedInput.substring(0, minOf(length - decimalPlaces + decimalCount, length)).toFloatOrNull()
}

fun Float?.toFormattedInput(trimDot: Boolean = true): String {
  var text = this?.toString() ?: ""
  text = if (text.lastOrNull() == '0') text.substring(0, text.lastIndex) else text
  return if (text.lastOrNull() == '.' && trimDot) text.substring(0, text.lastIndex) else text
}

fun Float.toFormattedText(decimals: Int = 3): String {
  var epsilon = 10F
  var multiplier = 1F
  repeat(decimals) {
    epsilon *= 0.1F
    multiplier *= 10
  }
  return if (this > 10 || abs(this - this.toInt()) < epsilon) {
    "${this.toInt()}"
  } else {
    val formattedAmount = ((this * multiplier).toInt() / multiplier).toString()
    Logger.e("Tester $formattedAmount")
    var index = formattedAmount.lastIndex
    while (index > 0 && formattedAmount[index] == '0') {
      index -= 1
    }

    formattedAmount.substring(0, index + 1)
  }
}
