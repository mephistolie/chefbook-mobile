package com.cactusknights.chefbook.ui.views.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.buttons.CircleImageButton
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerDefaults
import com.google.accompanist.pager.rememberPagerState
import dev.chrisbanes.snapper.ExperimentalSnapperApi

@OptIn(ExperimentalComposeUiApi::class, ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun FullscreenPicturesDialog(
    pictures: List<String>,
    startIndex: Int = 0,
    onHide: () -> Unit,
) {
    val context = LocalContext.current
    val colors = ChefBookTheme.colors

    val pagerState = rememberPagerState(startIndex)

    Dialog(
        onDismissRequest = onHide,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(1.25F)
                .background(colors.backgroundPrimary)
                .scale(0.8F),
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                count = pictures.size,
                state = pagerState,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                flingBehavior = PagerDefaults.flingBehavior(
                    pagerState,
                    endContentPadding = 0.dp
                )
            ) { pictureIndex ->
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(pictures[pictureIndex])
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                activeColor = colors.foregroundPrimary,
                inactiveColor = colors.backgroundTertiary,
                indicatorWidth = 6.dp,
            )
            CircleImageButton(
                image = ImageVector.vectorResource(R.drawable.ic_cross),
                onClick = onHide,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(48.dp),
                background = Color.Transparent,
                tint = colors.foregroundPrimary,
            )
        }
    }
}
