package com.mysty.chefbook.ui.common.dialogs.pictures

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerDefaults
import com.google.accompanist.pager.rememberPagerState
import com.mephistolie.compost.ui.buttons.CircleIconButton
import com.mysty.chefbook.core.android.compose.providers.ContentAccessProvider
import com.mysty.chefbook.core.android.compose.providers.ContentType
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.R
import com.mysty.chefbook.design.components.images.EncryptedImage
import com.mysty.chefbook.design.theme.shapes.RoundedCornerShape24
import com.mysty.chefbook.navigation.navigators.IBaseNavigator
import com.mysty.chefbook.navigation.styles.DismissibleDialog
import com.ramcosta.composedestinations.annotation.Destination
import dev.chrisbanes.snapper.ExperimentalSnapperApi

@Destination(route = "pictures", style = DismissibleDialog::class)
@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun PicturesViewer(
    pictures: Array<String>,
    startIndex: Int = 0,
    picturesType: ContentType = ContentType.DECRYPTED,
    navigator: IBaseNavigator,
) {
    val colors = LocalTheme.colors

    val pagerState = rememberPagerState(startIndex)

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
            ContentAccessProvider(contentType = picturesType) {
                EncryptedImage(
                    data = pictures[pictureIndex],
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .clip(RoundedCornerShape24)
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
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
            onClick = navigator::navigateUp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .size(48.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            tint = Color.White,
        )
    }
}
