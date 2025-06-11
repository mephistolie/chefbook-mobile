package io.chefbook.features.recipe.info.ui.components.common.content.loaded

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.core.android.utils.EmojiUtils
import io.chefbook.design.components.images.EncryptedImage
import io.chefbook.design.components.spacers.HorizontalSpacer
import io.chefbook.design.components.spacers.VerticalSpacer
import io.chefbook.design.theme.colors.Monochrome96
import io.chefbook.design.theme.shapes.SmoothCornerShape28
import io.chefbook.features.recipe.info.ui.components.common.actions.ActionsWidget
import io.chefbook.features.recipe.info.ui.components.common.actions.buttons.CloseButton
import io.chefbook.features.recipe.info.ui.components.common.actions.buttons.LanguageButton
import io.chefbook.features.recipe.info.ui.components.common.actions.buttons.ProfileButton
import io.chefbook.features.recipe.info.ui.components.common.actions.buttons.ShareButton
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenIntent
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import kotlin.math.abs

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
internal fun RecipeScreenSurfaceContent(
  state: RecipeScreenState.Success,
  onIntent: (RecipeScreenIntent) -> Unit,
  bottomSheetState: BottomSheetState? = null,
  setCardHeight: (Dp) -> Unit = {},
) {
  val density = LocalDensity.current

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val recipe = state.recipe

  val expandTransition = updateTransition(bottomSheetState?.targetValue, label = "isPreviewLoaded")
  val cardScale by expandTransition.animateFloat(label = "isExpanded") { targetValue ->
    val currentValue = bottomSheetState?.currentValue
    when {
      currentValue == targetValue ->
        when {
          targetValue == BottomSheetValue.Expanded -> 0.9F
          else -> 1F
        }

      else ->
        when {
          targetValue == BottomSheetValue.Expanded -> 1F - 0.1F * abs(
            bottomSheetState?.progress ?: 0F
          )

          else -> 0.9F + 0.1F * abs(bottomSheetState?.progress ?: 0F)
        }
    }
  }

  Box(
    modifier = Modifier
      .run {
        if (recipe.preview?.isNotBlank() == true) {
          aspectRatio(1 / 1.1F)
        } else {
          fillMaxWidth()
            .wrapContentHeight()
        }
      }
      .scale(cardScale)
      .clippedBackground(colors.backgroundPrimary, SmoothCornerShape28)
      .onGloballyPositioned {coordinates ->
        with(density) { setCardHeight(coordinates.size.height.toDp()) }
      },
    contentAlignment = Alignment.Center,
  ) {
    val isPreviewLoaded = remember { mutableStateOf(recipe.preview != null) }
    val transition = updateTransition(isPreviewLoaded.value, label = "isPreviewLoaded")

    val gradientColor by transition.animateColor(label = "gradientColor") { isLoaded ->
      if (isLoaded) Color.Black else Color.Transparent
    }
    val foregroundPrimary by transition.animateColor(label = "foregroundPrimary") { isLoaded ->
      if (isLoaded) Color.White else colors.foregroundPrimary
    }
    val foregroundSecondary by transition.animateColor(label = "foregroundSecondary") { isLoaded ->
      if (isLoaded) Monochrome96 else colors.foregroundSecondary
    }

    val coverPlaceholder = remember { EmojiUtils.randomFoodEmoji(recipe.id) }

    recipe.preview?.let { preview ->
      Text(
        text = coverPlaceholder,
        style = TextStyle(fontSize = TextUnit(72F, TextUnitType.Sp)),
        textAlign = TextAlign.Center,
        modifier = Modifier.alpha(0.85F),
      )
      EncryptedImage(
        data = preview,
        onSuccess = { isPreviewLoaded.value = true },
        onError = { isPreviewLoaded.value = false },
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1F),
      )
      EncryptedImage(
        data = preview,
        contentScale = ContentScale.FillBounds,
        onSuccess = { isPreviewLoaded.value = true },
        onError = { isPreviewLoaded.value = false },
        modifier = Modifier
          .fillMaxSize()
          .blur(20.dp)
          .graphicsLayer { alpha = 0.99f }
          .drawWithContent {
            drawContent()

            drawRect(
              brush = Brush.verticalGradient(
                0.1F to gradientColor,
                0.15F to Color.Transparent,
                0.7F to Color.Transparent,
                0.8F to gradientColor,
              ),
              blendMode = BlendMode.DstIn
            )

            val shadowColor = gradientColor.copy(alpha = gradientColor.alpha * 0.2F)
            drawRect(
              brush = Brush.verticalGradient(
                0.1F to shadowColor,
                0.15F to Color.Transparent,
                0.7F to Color.Transparent,
                0.8F to shadowColor,
              ),
            )
          },
      )
    }
    Column(
      modifier = Modifier
        .run {
          if (recipe.preview?.isNotBlank() == true) {
            matchParentSize()
          } else {
            fillMaxWidth()
              .wrapContentHeight()
          }
        }
        .padding(12.dp),
      verticalArrangement = Arrangement.SpaceBetween,
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
        ) {
          ProfileButton(
            name = recipe.owner.name.orEmpty(),
            avatar = recipe.owner.avatar,
            preview = recipe.preview,
            isPreviewLoaded = isPreviewLoaded,
            onLanguageClick = {}
          )
          HorizontalSpacer(width = 8.dp)
          LanguageButton(
            language = recipe.language,
            preview = recipe.preview,
            isPreviewLoaded = isPreviewLoaded,
            onLanguageClick = {}
          )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          ShareButton(
            preview = recipe.preview,
            isPreviewLoaded = isPreviewLoaded,
            onShareClick = { onIntent(RecipeScreenIntent.OpenShareDialog) },
          )
          CloseButton(
            preview = recipe.preview,
            isPreviewLoaded = isPreviewLoaded,
            onCloseClick = { onIntent(RecipeScreenIntent.Close) },
          )
        }
      }
      Column(
        modifier = Modifier
          .padding(top = 20.dp)
          .fillMaxWidth(),
      ) {
        Row(
          modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          Column(
            modifier = Modifier
              .weight(1F)
              .fillMaxWidth(),
          ) {
            Text(
              text = state.recipe.name,
              maxLines = 2,
              style = typography.h2,
              color = foregroundPrimary,
            )
            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
              state.recipe.tags.forEachIndexed { index, tag ->
                Text(
                  text = tag.name,
                  maxLines = 1,
                  style = typography.headline1,
                  color = foregroundSecondary,
                )
                if (index < state.recipe.tags.lastIndex) {
                  Text(
                    text = "•",
                    maxLines = 1,
                    style = typography.headline2,
                    color = foregroundSecondary,
                  )
                }
              }
            }
//          VerticalSpacer(8.dp)
//          Row(
//            verticalAlignment = Alignment.CenterVertically,
//          ) {
//            ProfileAvatar(
//              url = state.recipe.owner.avatar,
//              size = 20.dp,
//            )
//            HorizontalSpacer(4.dp)
//            Text(
//              text = state.recipe.owner.name.orEmpty(),
//              maxLines = 1,
//              style = typography.headline1,
//              color = foregroundSecondary,
//            )
//          }
          }
        }
        VerticalSpacer(16.dp)
        ActionsWidget(
          recipe = state.recipe,
          isPreviewLoaded = isPreviewLoaded,
          onRateClick = { onIntent(RecipeScreenIntent.RateButtonClicked) },
          onSaveClick = {
            if (!state.recipe.isSaved) {
              onIntent(RecipeScreenIntent.AddToRecipeBook)
            } else {
              onIntent(RecipeScreenIntent.OpenRecipeMenu)
            }
          },
          onInfoClick = { onIntent(RecipeScreenIntent.OpenShareDialog) },
        )
      }
    }
  }
}
