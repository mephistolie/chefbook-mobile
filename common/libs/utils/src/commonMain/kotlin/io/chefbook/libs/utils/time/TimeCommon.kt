package io.chefbook.libs.utils.time

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun parseInstantSafely(time: String?): Instant? {
  if (time.isNullOrBlank()) return null
  return try {
    Instant.parse(time)
  } catch (e: Exception) {
    null
  }
}

fun parseTimestampSafely(time: String?): LocalDateTime? =
  parseInstantSafely(time)?.toLocalDateTime(TimeZone.UTC)

fun parseTimestampOrNow(time: String?) =
  parseTimestampSafely(time) ?: Clock.System.now().toLocalDateTime(TimeZone.UTC)
