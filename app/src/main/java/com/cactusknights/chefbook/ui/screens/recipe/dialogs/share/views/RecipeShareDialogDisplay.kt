package com.cactusknights.chefbook.ui.screens.recipe.dialogs.share.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.screens.recipe.dialogs.share.models.RecipeShareDialogEvent
import com.cactusknights.chefbook.ui.screens.recipe.dialogs.share.models.RecipeShareDialogState
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.buttons.DynamicButton
import com.cactusknights.chefbook.ui.views.textfields.CopyLinkField
import com.mephistolie.compost.ui.buttons.CircleIconButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecipeShareDialogDisplay(
    state: RecipeShareDialogState,
    onEvent: (RecipeShareDialogEvent) -> Unit,
    onHide: () -> Unit,
) {
    val context = LocalContext.current
    val resources = context.resources
    val coroutine = rememberCoroutineScope()

    val typography = ChefBookTheme.typography
    val colors = ChefBookTheme.colors

    Dialog(
        onDismissRequest = onHide,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .wrapContentHeight()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = colors.backgroundPrimary,
                        shape = RoundedCornerShape(32.dp, 32.dp, 24.dp, 24.dp)
                    ),
                contentAlignment = Alignment.TopEnd
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${stringResource(R.string.common_general_recipe)} #${state.id}",
                        modifier = Modifier.padding(top = 16.dp),
                        maxLines = 2,
                        style = typography.h2,
                        color = colors.foregroundPrimary,
                    )
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(state.qr)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(24.dp, 18.dp, 24.dp)
                            .aspectRatio(1F)
                            .fillMaxSize()
                    )
                    Divider(
                        color = colors.backgroundSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                            .height(1.dp)
                    )
                    CopyLinkField(
                        link = state.url.orEmpty(),
                        onCopy = { onEvent(RecipeShareDialogEvent.CopyLink) },
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxWidth()
                            .height(48.dp),
                    )
                    DynamicButton(
                        leftIcon = ImageVector.vectorResource(R.drawable.ic_message),
                        text = stringResource(R.string.common_recipe_share_dialog_send_as_text),
                        isSelected = true,
                        modifier = Modifier
                            .padding(24.dp, 16.dp, 24.dp, 24.dp)
                            .fillMaxWidth()
                            .height(48.dp),
                        onClick = { onEvent(RecipeShareDialogEvent.CopyAsText(resources)) },
                    )
                }
                CircleIconButton(
                    icon = ImageVector.vectorResource(R.drawable.ic_cross),
                    onClick = { coroutine.launch { onHide() } },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(32.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colors.foregroundPrimary.copy(alpha = 0.25F)),
                    tint = Color.White,
                )
            }
        }
    }
}
