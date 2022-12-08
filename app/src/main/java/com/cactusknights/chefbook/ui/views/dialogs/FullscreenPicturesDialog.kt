package com.cactusknights.chefbook.ui.views.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.images.EncryptedImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerDefaults
import com.google.accompanist.pager.rememberPagerState
import com.mephistolie.compost.ui.buttons.CircleIconButton
import dev.chrisbanes.snapper.ExperimentalSnapperApi

@OptIn(ExperimentalComposeUiApi::class, ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun FullscreenPicturesDialog(
    pictures: List<String>,
    startIndex: Int = 0,
    onHide: () -> Unit,
) {
    val colors = ChefBookTheme.colors

    val pagerState = rememberPagerState(startIndex)

    Dialog(
        onDismissRequest = onHide,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
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
                EncryptedImage(
                    data = pictures[pictureIndex],
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
//                        .shadow(elevation = 12.dp, shape = RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
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
                inactiveColor = colors.foregroundSecondary,
                indicatorWidth = 6.dp,
            )
            CircleIconButton(
                icon = ImageVector.vectorResource(R.drawable.ic_cross),
                onClick = onHide,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(48.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                tint = Color.White,
            )
        }
    }
}
