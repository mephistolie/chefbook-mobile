package io.chefbook.core.utils

import androidx.compose.runtime.Composable
import io.chefbook.core.Res
import io.chefbook.core.commonGeneralHours
import io.chefbook.core.commonGeneralMinutes
import org.jetbrains.compose.resources.stringResource

@Composable
fun minutesToTimeString(minutes: Int?): String =
  buildString {
    if (minutes == null) return@buildString

    if (minutes >= 60) {
      append(minutes / 60)
      append(" ")
      stringResource(Res.string.commonGeneralHours)
    }

    if (minutes % 60 != 0) {
      if (minutes >= 60) {
        append(" ")
      }

      append(minutes % 60)
      append(" ")
      append(stringResource(Res.string.commonGeneralMinutes))
    }
  }
