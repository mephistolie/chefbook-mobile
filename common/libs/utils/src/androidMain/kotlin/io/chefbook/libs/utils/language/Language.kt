package io.chefbook.libs.utils.language

import java.util.Locale

actual fun getSystemLanguageCode(): String =
  Locale.getDefault().language
