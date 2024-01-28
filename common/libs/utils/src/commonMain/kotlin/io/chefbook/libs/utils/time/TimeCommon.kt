package io.chefbook.libs.utils.time

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun parseTimestampSafely(time: String?) =
  try {
    Instant.parse(time.orEmpty()).toLocalDateTime(TimeZone.UTC)
  } catch (e: Exception) {
    null
  }

fun parseTimestampOrNow(time: String?) =
  try {
    Instant.parse(time.orEmpty()).toLocalDateTime(TimeZone.UTC)
  } catch (e: Exception) {
    Clock.System.now().toLocalDateTime(TimeZone.UTC)
  }