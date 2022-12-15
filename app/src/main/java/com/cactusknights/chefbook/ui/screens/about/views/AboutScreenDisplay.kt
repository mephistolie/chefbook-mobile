package com.cactusknights.chefbook.ui.screens.about.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.BuildConfig
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.screens.about.models.AboutScreenEvent
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.dividers.Divider
import com.mysty.chefbook.design.components.toolbar.Toolbar

@Composable
fun AboutScreenDisplay(
    onEvent: (AboutScreenEvent) -> Unit,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Box(
        modifier = Modifier
            .background(colors.backgroundPrimary)
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Toolbar(
                modifier = Modifier.padding(horizontal = 12.dp),
                onLeftButtonClick = { onEvent(AboutScreenEvent.Back) },
            ) {
                Text(
                    text = stringResource(R.string.common_about_title),
                    style = typography.h4,
                    color = colors.foregroundPrimary,
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_broccy),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .size(128.dp),
                    )
                }
                item {
                    Text(
                        text = stringResource(R.string.common_about_version, BuildConfig.VERSION_NAME),
                        style = typography.body2,
                        color = colors.foregroundSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                item {
                    Text(
                        text = stringResource(R.string.common_about_made_by),
                        style = typography.headline2,
                        color = colors.foregroundSecondary,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                }
                item {
                    Text(
                        text = stringResource(R.string.common_about_dedicated_to),
                        style = typography.headline2,
                        color = colors.foregroundSecondary,
                        modifier = Modifier.padding(bottom = 36.dp)
                    )
                    Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 8.dp))
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 16.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Обратная связь",
                            style = typography.headline1,
                            color = colors.foregroundPrimary,
                            modifier = Modifier.wrapContentHeight()
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                            tint = colors.foregroundPrimary,
                            modifier = Modifier
                                .size(18.dp),
                            contentDescription = null,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 16.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Оценить приложение",
                            style = typography.headline1,
                            color = colors.foregroundPrimary,
                            modifier = Modifier.wrapContentHeight()
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                            tint = colors.foregroundPrimary,
                            modifier = Modifier
                                .size(18.dp),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}
