package io.chefbook.sdk.database.api.internal

fun Long.toBoolean() = this == 1L

fun Boolean.toLong() = if (this) 1L else 0L