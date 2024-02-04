package io.chefbook.libs.utils.auth

private val nicknameRegex by lazy { """^[a-zA-Z0-9_]+$""".toRegex() }
private val nicknameStartLetterRegex by lazy { """^[a-zA-Z]$""".toRegex() }
private val nicknameEndLetterRegex by lazy { """^[a-zA-Z0-9]$""".toRegex() }

private val numberValidator by lazy { """^(?=.*[0-9]).{8,}$""".toRegex() }
private val lowerValidator by lazy { """^(?=.*[a-z]).{8,}$""".toRegex() }
private val upperValidator by lazy { """^(?=.*[A-Z]).{8,}$""".toRegex() }
private val spaceValidator by lazy { """^(?=\S+$).+""".toRegex() }

fun isEmail(str: String): Boolean {
  val atIndex = str.indexOf("@")
  return atIndex >= 0 && atIndex < str.indexOf(".") && str.lastOrNull() != '.' && str.length >= 5
}

fun isNickname(str: String) = str.length >= 5
    && str.matches(nicknameRegex)
    && str.first().toString().matches(nicknameStartLetterRegex)
    && str.last().toString().matches(nicknameEndLetterRegex)

fun isNicknameSymbols(str: String) = str.isEmpty() || str.matches(nicknameRegex)
fun isFirstNicknameSymbol(char: Char?) = char == null || char.toString().matches(nicknameStartLetterRegex)
fun isLastNicknameSymbol(char: Char?) = char == null || char.toString().matches(nicknameEndLetterRegex)

fun validatePassword(password: String, repeatPassword: String): PasswordRating {
  if (password.isNotEmpty() && !password.matches(spaceValidator)) return PasswordRating.SPACE
  if (password.length < 8) return PasswordRating.SHORT
  if (!password.matches(lowerValidator)) return PasswordRating.LOWER
  if (!password.matches(upperValidator)) return PasswordRating.UPPER
  if (!password.matches(numberValidator)) return PasswordRating.NUMBER
  if (password != repeatPassword) return PasswordRating.NOT_MATCH

  return PasswordRating.VALID
}

enum class PasswordRating {
  VALID, SHORT, UPPER, LOWER, NUMBER, SPACE, NOT_MATCH
}
