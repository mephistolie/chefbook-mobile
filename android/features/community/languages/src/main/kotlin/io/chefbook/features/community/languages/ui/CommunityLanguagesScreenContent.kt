package io.chefbook.features.community.languages.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.checkboxes.Checkbox
import io.chefbook.design.theme.shapes.RoundedCornerShape28Top
import io.chefbook.features.community.languages.ui.mvi.CommunityLanguagesScreenIntent
import io.chefbook.libs.models.language.Language
import io.chefbook.ui.common.extensions.localizedName
import io.chefbook.core.android.R as coreR

@Composable
internal fun CommunityLanguagesScreenContent(
  selectedLanguages: List<Language>,
  onIntent: (CommunityLanguagesScreenIntent) -> Unit,
) {
  val resources = LocalContext.current.resources

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = Modifier
      .statusBarsPadding()
      .clippedBackground(colors.backgroundPrimary, shape = RoundedCornerShape28Top)
      .padding(horizontal = 18.dp),
  ) {
    Text(
      text = stringResource(coreR.string.common_general_languages),
      maxLines = 1,
      style = typography.h4,
      color = colors.foregroundPrimary,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
    )
    Divider(
      color = colors.backgroundSecondary,
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
    )
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
    ) {
      for (language in Language.entries) {
        item {
          Row(
            modifier = Modifier
              .padding(top = 24.dp)
              .fillMaxWidth()
              .wrapContentHeight()
              .simpleClickable {
                onIntent(
                  if (language !in selectedLanguages) {
                    CommunityLanguagesScreenIntent.LanguageSelected(language)
                  } else {
                    CommunityLanguagesScreenIntent.LanguageUnselected(language)
                  }
                )
              },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Text(
              text = "${language.flag} ${language.localizedName(resources)}",
              style = typography.headline1,
              color = colors.foregroundPrimary,
            )
            Checkbox(
              isChecked = language in selectedLanguages,
              onClick = {},
              isEnabled = false,
            )
          }
        }
      }
      item {
        Spacer(
          modifier = Modifier
            .navigationBarsPadding()
            .height(36.dp)
        )
      }
    }
  }
}