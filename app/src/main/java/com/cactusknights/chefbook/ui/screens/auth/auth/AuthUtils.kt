package com.cactusknights.chefbook.ui.screens.auth.auth

object AuthUtils {
    fun validateEmail(email: String): Boolean =
        email.contains("@") && email.contains(".") && email.length >= 5

    fun validatePassword(password: String, repeatPassword: String): Password {

        val numberValidator = """^(?=.*[0-9]).{8,}$""".toRegex()
        val lowerValidator = """^(?=.*[a-z]).{8,}$""".toRegex()
        val upperValidator = """^(?=.*[A-Z]).{8,}$""".toRegex()
        val spaceValidator = """^(?=\S+$).+""".toRegex()

        if (password.isNotEmpty() && !password.matches(spaceValidator)) return Password.SPACE
        if (password.length < 8) return Password.SHORT
        if (!password.matches(lowerValidator)) return Password.LOWER
        if (!password.matches(upperValidator)) return Password.UPPER
        if (!password.matches(numberValidator)) return Password.NUMBER
        if (password != repeatPassword) return Password.NOT_MATCH

        return Password.VALID
    }
}

enum class Password {
    VALID, SHORT, UPPER, LOWER, NUMBER, SPACE, NOT_MATCH
}
