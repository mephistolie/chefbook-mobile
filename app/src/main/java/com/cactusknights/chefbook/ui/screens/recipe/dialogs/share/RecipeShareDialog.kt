package com.cactusknights.chefbook.ui.screens.recipe.dialogs.share

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
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.ui.screens.recipe.dialogs.share.models.RecipeShareDialogEffect
import com.cactusknights.chefbook.ui.screens.recipe.dialogs.share.models.RecipeShareDialogEvent
import com.cactusknights.chefbook.ui.screens.recipe.dialogs.share.views.RecipeShareDialogDisplay
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.themes.Monochrome20
import com.cactusknights.chefbook.ui.themes.Monochrome30
import org.koin.androidx.compose.getViewModel

@Composable
fun RecipeShareDialog(
    recipe: Recipe,
    onHide: () -> Unit,
    viewModel: RecipeShareDialogViewModel = getViewModel(),
) {
    val context = LocalContext.current
    val resources = context.resources
    val clipboardManager = LocalClipboardManager.current
    val colors = ChefBookTheme.colors

    val state = viewModel.state.collectAsState()

    RecipeShareDialogDisplay(
        state = state.value,
        onEvent = { event -> viewModel.obtainEvent(event) },
        onHide = onHide,
    )

    LaunchedEffect(Unit) {
        viewModel.obtainEvent(
            RecipeShareDialogEvent.LoadShareData(
                recipe = recipe,
                context = context,
                startColor = if (colors.isDark) colors.foregroundPrimary.toArgb() else Monochrome30.toArgb(),
                endColor = if (colors.isDark) colors.foregroundSecondary.toArgb() else Monochrome20.toArgb(),
            )
        )

        viewModel.effect.collect { effect ->
            when (effect) {
                is RecipeShareDialogEffect.CopyText -> {
                    clipboardManager.setText(AnnotatedString(effect.text))
                    Toast.makeText(context, resources.getString(effect.messageId), Toast.LENGTH_SHORT).show()
                }
                is RecipeShareDialogEffect.ShareText -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
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
