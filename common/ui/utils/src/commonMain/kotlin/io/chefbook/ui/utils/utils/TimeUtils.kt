package io.chefbook.ui.utils.utils

import androidx.compose.runtime.Composable
import io.chefbook.ui.utils.Res
import io.chefbook.ui.utils.commonGeneralHours
import io.chefbook.ui.utils.commonGeneralMinutes
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
