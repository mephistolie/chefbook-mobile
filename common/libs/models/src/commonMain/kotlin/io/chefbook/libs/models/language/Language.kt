package io.chefbook.libs.models.language

enum class Language(val code: String, val flag: String?) {
  ENGLISH(code = "en", flag = "\uD83C\uDDFA\uD83C\uDDF8"),
  RUSSIAN(code = "ru", flag = "\uD83C\uDDF7\uD83C\uDDFA"),
  UKRAINIAN(code = "uk", flag = "\uD83C\uDDFA\uD83C\uDDE6"),
  BELARUSIAN(code = "be", flag = "\uD83C\uDDE7\uD83C\uDDFE"),
  KAZAKH(code = "kk", flag = "\uD83C\uDDF0\uD83C\uDDFF"),
  ARMENIAN(code = "hy", flag = "\uD83C\uDDE6\uD83C\uDDF2"),
  GEORGIAN(code = "ka", flag = "\uD83C\uDDEC\uD83C\uDDEA"),
  SERBIAN(code = "sr", flag = "\uD83C\uDDF7\uD83C\uDDF8"),
  FRENCH(code = "fr", flag = "\uD83C\uDDEB\uD83C\uDDF7"),
  GERMAN(code = "de", flag = "\uD83C\uDDE9\uD83C\uDDEA"),
  ITALIAN(code = "it", flag = "\uD83C\uDDEE\uD83C\uDDF9"),
  SPANISH(code = "es", flag = "\uD83C\uDDEA\uD83C\uDDF8"),
  TURKISH(code = "tr", flag = "\uD83C\uDDF9\uD83C\uDDF7"),
  HINDI(code = "hi", flag = "\uD83C\uDDEE\uD83C\uDDF3"),
  PORTUGUESE(code = "pt", flag = "\uD83C\uDDF5\uD83C\uDDF9"),
  BENGALI(code = "bn", flag = "\uD83C\uDDE7\uD83C\uDDE9"),
  JAPANESE(code = "ja", flag = "\uD83C\uDDEF\uD83C\uDDF5"),
  KOREAN(code = "ko", flag = "\uD83C\uDDF0\uD83C\uDDF7"),
  CHINESE(code = "zh", flag = "\uD83C\uDDE8\uD83C\uDDF3"),
  ARABIAN(code = "ar", flag = "\uD83C\uDDE6\uD83C\uDDEA"),
  PERSIAN(code = "fa", flag = "\uD83C\uDDEE\uD83C\uDDF7"),
  OTHER(code = "ns", flag = "\uD83C\uDFF3️")
}
