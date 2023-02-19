package com.mysty.chefbook.features.recipe.share.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.core.content.ContextCompat
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.theme.colors.Monochrome20
import com.mysty.chefbook.design.theme.colors.Monochrome30
import com.mysty.chefbook.features.recipe.share.ui.mvi.RecipeShareDialogEffect
import com.mysty.chefbook.features.recipe.share.ui.mvi.RecipeShareDialogIntent
import com.mysty.chefbook.navigation.navigators.IBaseNavigator
import com.mysty.chefbook.navigation.styles.DismissibleDialog
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

private const val TEXT_TYPE = "text/plain"

@Destination(route = "share", style = DismissibleDialog::class)
@Composable
internal fun RecipeShareDialog(
  recipeId: String,
  navigator: IBaseNavigator,
) {
  val context = LocalContext.current
  val resources = context.resources
  val clipboardManager = LocalClipboardManager.current
  val colors = LocalTheme.colors

  val viewModel: IRecipeShareDialogViewModel =
    getViewModel<RecipeShareDialogViewModel> { parametersOf(recipeId) }
  val state = viewModel.state.collectAsState()

  RecipeShareDialogContent(
    state = state.value,
    onIntent = { event -> viewModel.handleIntent(event) },
  )

  LaunchedEffect(colors.isDark) {
    viewModel.handleIntent(
      RecipeShareDialogIntent.RenderQR(
        startColor = if (colors.isDark) colors.foregroundPrimary.toArgb() else Monochrome30.toArgb(),
        endColor = if (colors.isDark) colors.foregroundSecondary.toArgb() else Monochrome20.toArgb(),
      )
    )
  }
  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is RecipeShareDialogEffect.Close -> navigator.navigateUp()
        is RecipeShareDialogEffect.CopyText -> {
          clipboardManager.setText(AnnotatedString(effect.text))
          Toast.makeText(
            context,
            resources.getString(effect.messageId),
            Toast.LENGTH_SHORT
          ).show()
        }
        is RecipeShareDialogEffect.ShareText -> {
          val intent = Intent(Intent.ACTION_SEND)
          intent.type = TEXT_TYPE
          intent.putExtra(Intent.EXTRA_TEXT, effect.text)

          ContextCompat.startActivity(
            context,
            Intent.createChooser(intent, null),
            null
          )
        }
      }
    }
  }
}
