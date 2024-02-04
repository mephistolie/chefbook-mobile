package io.chefbook.features.recipe.input.ui.dialogs.language

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
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
import io.chefbook.design.components.radibuttons.RadioButton
import io.chefbook.design.theme.shapes.ModalBottomSheetShape
import io.chefbook.features.recipe.input.R
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputDetailsScreenIntent
import io.chefbook.libs.models.language.Language
import io.chefbook.ui.common.extensions.localizedName
import io.chefbook.core.android.R as coreR

@Composable
internal fun LanguageDialogContent(
  selectedLanguage: Language,
  onIntent: (RecipeInputDetailsScreenIntent) -> Unit,
) {
  val resources = LocalContext.current.resources

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = Modifier
      .clippedBackground(colors.backgroundPrimary, shape = ModalBottomSheetShape)
      .padding(horizontal = 18.dp)
      .fillMaxWidth()
      .wrapContentHeight(),
  ) {
    Text(
      text = stringResource(coreR.string.common_general_language),
      maxLines = 1,
      style = typography.h4,
      color = colors.foregroundPrimary,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
    )
    Text(
      text = stringResource(R.string.common_recipe_input_screen_language_dialog_description),
      style = typography.subhead1,
      color = colors.foregroundSecondary,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 4.dp, bottom = 12.dp)
    )
    Divider(
      color = colors.backgroundSecondary,
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
    )
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .requiredHeightIn(0.dp, 512.dp)
    ) {
      for (language in Language.values()) {
        item {
          Row(
            modifier = Modifier
              .padding(top = 24.dp)
              .fillMaxWidth()
              .wrapContentHeight()
              .simpleClickable { onIntent(RecipeInputDetailsScreenIntent.SetLanguage(language)) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Text(
              text = "${language.flag}  ${language.localizedName(resources)}",
              style = typography.headline1,
              color = colors.foregroundPrimary,
            )
            RadioButton(
              isSelected = selectedLanguage == language,
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