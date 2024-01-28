package io.chefbook.libs.models.language

object LanguageMapper {

  private val languageMap = mapOf(
    Language.ENGLISH.code to Language.ENGLISH,
    Language.RUSSIAN.code to Language.RUSSIAN,
    Language.UKRAINIAN.code to Language.UKRAINIAN,
    Language.BELARUSIAN.code to Language.BELARUSIAN,
    Language.FRENCH.code to Language.FRENCH,
    Language.GERMAN.code to Language.GERMAN,
    Language.ITALIAN.code to Language.ITALIAN,
    Language.SPANISH.code to Language.SPANISH,
    Language.TURKISH.code to Language.TURKISH,
    Language.HINDI.code to Language.HINDI,
    Language.PORTUGUESE.code to Language.PORTUGUESE,
    Language.BENGALI.code to Language.BENGALI,
    Language.JAPANESE.code to Language.JAPANESE,
    Language.KOREAN.code to Language.KOREAN,
    Language.CHINESE.code to Language.CHINESE,
    Language.ARABIAN.code to Language.ARABIAN,
    Language.PERSIAN.code to Language.PERSIAN,
  )

  fun map(code: String?) = languageMap[code] ?: Language.OTHER
}
