package io.chefbook.features.community.recipes.ui.screens.content.components.blocks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.R as coreR
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.theme.dimens.ButtonSmallHeight
import io.chefbook.design.theme.dimens.DefaultIconSize
import io.chefbook.design.theme.dimens.ToolbarHeight
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenState
import io.chefbook.features.community.recipes.ui.mvi.DashboardState
import io.chefbook.libs.models.language.Language
import io.chefbook.ui.common.extensions.localizedName
import io.chefbook.design.R as designR

@Composable
internal fun Toolbar(
  languages: List<Language>,
  avatar: String?,
  mode: CommunityRecipesScreenState.Mode,
  tab: DashboardState.Tab,
  isRecipesBlockExpanded: Boolean,
  onBackClick: () -> Unit,
  onLanguageClick: () -> Unit,
  onProfileClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val resources = context.resources
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clippedBackground(
        colors.backgroundPrimary,
        RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
      )
      .statusBarsPadding()
      .height(ToolbarHeight)
      .padding(vertical = 8.dp, horizontal = 12.dp),
    contentAlignment = Alignment.Center,
  ) {
    Icon(
      imageVector = ImageVector.vectorResource(designR.drawable.ic_arrow_left),
      tint = colors.foregroundPrimary,
      contentDescription = null,
      modifier = Modifier
        .align(Alignment.CenterStart)
        .size(DefaultIconSize)
        .simpleClickable(onClick = onBackClick),
    )

    AnimatedVisibility(
      visible = !isRecipesBlockExpanded,
      enter = fadeIn() + slideInVertically(),
      exit = fadeOut() + slideOutVertically(),
    ) {
      DynamicButton(
        text = when {
          languages.isEmpty() -> stringResource(coreR.string.common_general_all_languages)
          languages.size == 1 -> "${languages[0].flag} ${languages[0].localizedName(resources)}"
          else -> {
            var flags = languages.take(5).fold("") { str, language -> str + language.flag }
            if (languages.size > 5) flags += "..."
            flags
          }
        },
        cornerRadius = 12.dp,
        textStyle = typography.headline1,
        iconsSize = 16.dp,
        rightIcon = ImageVector.vectorResource(designR.drawable.ic_arrow_down),
        selectedForeground = colors.foregroundPrimary,
        selectedBackground = Color.Transparent,
        isSelected = true,
        useSimpleClickable = true,
        modifier = Modifier.height(ButtonSmallHeight),
        onClick = onLanguageClick,
        debounceInterval = 1000L,
      )
    }
    AnimatedVisibility(
      visible = isRecipesBlockExpanded,
      enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
      exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
    ) {
      DynamicButton(
        text =
        when (mode) {
          CommunityRecipesScreenState.Mode.FILTER -> stringResource(coreR.string.common_general_search)
          CommunityRecipesScreenState.Mode.DASHBOARD -> when (tab) {
            DashboardState.Tab.NEW -> stringResource(coreR.string.common_general_new)
            DashboardState.Tab.VOTES -> stringResource(coreR.string.common_general_popular)
            DashboardState.Tab.TOP -> stringResource(coreR.string.common_general_top)
            DashboardState.Tab.FAST -> stringResource(coreR.string.common_general_fast)
          }
        },
        cornerRadius = 12.dp,
        textStyle = typography.headline1,
        iconsSize = 16.dp,
        selectedForeground = colors.foregroundPrimary,
        selectedBackground = Color.Transparent,
        isSelected = true,
        useSimpleClickable = true,
        modifier = Modifier.height(ButtonSmallHeight),
        onClick = {},
      )
    }

    AsyncImage(
      model = ImageRequest.Builder(context)
        .data(avatar)
        .crossfade(true)
        .build(),
      placeholder = painterResource(designR.drawable.ic_user_circle),
      error = painterResource(designR.drawable.ic_user_circle),
      contentDescription = null,
      contentScale = ContentScale.Crop,
      colorFilter = if (avatar.isNullOrBlank()) ColorFilter.tint(LocalTheme.colors.foregroundPrimary) else null,
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .size(28.dp)
        .clip(CircleShape)
        .simpleClickable(1000L, onProfileClick)
    )
  }
}
