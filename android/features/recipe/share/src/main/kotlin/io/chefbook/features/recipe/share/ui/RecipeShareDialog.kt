package io.chefbook.features.recipe.share.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.core.content.ContextCompat
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.colors.Monochrome20
import io.chefbook.design.theme.colors.Monochrome30
import io.chefbook.features.recipe.share.ui.mvi.RecipeShareDialogEffect
import io.chefbook.features.recipe.share.ui.mvi.RecipeShareDialogIntent
import io.chefbook.navigation.navigators.BaseNavigator
import io.chefbook.navigation.styles.DismissibleDialog
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private const val TEXT_TYPE = "text/plain"

@Destination(route = "share", style = DismissibleDialog::class)
@Composable
internal fun RecipeShareDialog(
  recipeId: String,
  navigator: BaseNavigator,
) {
  val context = LocalContext.current
  val resources = context.resources
  val clipboardManager = LocalClipboardManager.current
  val colors = LocalTheme.colors

  val viewModel: IRecipeShareDialogViewModel =
    koinViewModel<RecipeShareDialogViewModel> { parametersOf(recipeId) }
  val state = viewModel.state.collectAsStateWithLifecycle()

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
