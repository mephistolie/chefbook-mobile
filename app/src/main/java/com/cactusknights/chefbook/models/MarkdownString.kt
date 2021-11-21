package com.cactusknights.chefbook.models

import java.io.Serializable

enum class MarkdownTypes {
    STRING, HEADER
}

data class MarkdownString (
    var text: String = "",
    val type: MarkdownTypes = MarkdownTypes.STRING
): Serializable