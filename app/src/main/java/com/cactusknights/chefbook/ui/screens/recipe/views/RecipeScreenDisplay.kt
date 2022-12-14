package com.cactusknights.chefbook.ui.screens.recipe.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.screens.recipe.dialogs.RecipeScreenDialogs
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenEvent
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenState
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenTab
import com.cactusknights.chefbook.ui.screens.recipe.views.blocks.ActionBlock
import com.cactusknights.chefbook.ui.screens.recipe.views.blocks.TabsBlock
import com.cactusknights.chefbook.ui.screens.recipe.views.pages.RecipeScreenPager
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.bottomsheet.BottomSheetSlider
import com.mysty.chefbook.design.components.buttons.BottomSheetCloseButton
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.design.components.images.EncryptedImage
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalPagerApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun RecipeScreenDisplay(
    state: RecipeScreenState,
    defaultTab: RecipeScreenTab,
    onEvent: (RecipeScreenEvent) -> Unit,
    onRefresh: () -> Unit,
    sheetState: ModalBottomSheetState,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    val coroutine = rememberCoroutineScope()

    val pagerState = rememberPagerState()

    val columnState = rememberLazyListState()
    val tabsBlockHeight = remember { mutableStateOf(0.dp) }

    Box(
        modifier = Modifier
            .background(colors.backgroundPrimary)
            .fillMaxWidth()
            .height(screenHeight),
        contentAlignment = Alignment.TopEnd
    ) {
        when (state) {
            is RecipeScreenState.Loading -> {
                CircularProgressIndicator(
                    color = colors.tintPrimary,
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.Center)
                )
            }
            is RecipeScreenState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    state = columnState,
                ) {
                    val recipe = state.recipe

                    item {
                        val topModifier =
                            if (recipe.preview != null)
                                Modifier
                                    .aspectRatio(1.25F)
                                    .background(colors.backgroundSecondary)
                            else
                                Modifier
                                    .fillMaxWidth()
                        Box(
                            modifier = topModifier,
                            contentAlignment = Alignment.TopCenter
                        ) {
                            recipe.preview?.let { preview ->
                                EncryptedImage(
                                    data = recipe.preview,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .simpleClickable {
                                            onEvent(
                                                RecipeScreenEvent.ChangeDialogState.Pictures(
                                                    isVisible = true,
                                                    selectedPicture = preview,
                                                )
                                            )
                                        },
                                )
                            }
                            BottomSheetSlider()
                        }
                    }
                    item {
                        Text(
                            text = state.recipe.name,
                            modifier = Modifier.padding(
                                start = 12.dp,
                                top = if (recipe.preview != null) 16.dp else 24.dp,
                                end = 12.dp
                            ),
                            maxLines = 3,
                            style = typography.h2,
                            color = colors.foregroundPrimary,
                        )
                    }
                    item {
                        ActionBlock(
                            state = state,
                            onLikeClicked = { onEvent(RecipeScreenEvent.ChangeLikeStatus) },
                            onSaveClicked = {
                                if (!state.recipe.isSaved) {
                                    onEvent(RecipeScreenEvent.ChangeSavedStatus)
                                } else {
                                    onEvent(RecipeScreenEvent.ChangeDialogState.RemoveFromRecipeBook(true))
                                }
                            },
                            onFavouriteClicked = { onEvent(RecipeScreenEvent.ChangeFavouriteStatus) },
                            onShareClicked = {
                                onEvent(RecipeScreenEvent.ChangeDialogState.Share(true))
                            },
                        )
                    }
                    stickyHeader {
                        TabsBlock(
                            tabs = tabs,
                            tabsBlockHeight = tabsBlockHeight,
                            columnState = columnState,
                            pagerState = pagerState,
                        )
                    }
                    item {
                        RecipeScreenPager(
                            state = state,
                            onCategoryClicked = { categoryId ->
                                onEvent(RecipeScreenEvent.OpenCategoryScreen(categoryId))
                            },
                            onChangeCategoriesClicked = {
                                onEvent(RecipeScreenEvent.ChangeCategories)
                            },
                            onCancelCategoriesSelectionClicked = {
                                onEvent(RecipeScreenEvent.DiscardCategoriesChanging)
                            },
                            onConfirmCategoriesSelectionClicked = { categories ->
                                onEvent(RecipeScreenEvent.ConfirmCategoriesChanging(categories))
                            },
                            onAddToShoppingListClicked = { ingredients, multiplier ->
                                onEvent(RecipeScreenEvent.AddIngredientsToShoppingList(ingredients, multiplier))
                            },
                            onStepPictureClicked = { picture ->
                                onEvent(
                                    RecipeScreenEvent.ChangeDialogState.Pictures(
                                        isVisible = true,
                                        selectedPicture = picture,
                                    )
                                )
                            },
                            onEditRecipeClicked = {
                                onEvent(RecipeScreenEvent.EditRecipe)
                            },
                            onDeleteRecipeClicked = {
                                onEvent(RecipeScreenEvent.ChangeDialogState.Delete(true))
                            },
                            pages = tabs,
                            pagerState = pagerState,
                            pageHeight = screenHeight - tabsBlockHeight.value,
                        )
                    }
                }
            }
            is RecipeScreenState.Error -> {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 64.dp, vertical = 24.dp)
                        .align(Alignment.Center)
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_broccy_grinning_sweat),
                        contentDescription = null,
                        modifier = Modifier.size(144.dp)
                    )
                    Text(
                        text = stringResource(R.string.common_recipe_screen_recipe_not_found),
                        style = typography.h2,
                        color = colors.foregroundPrimary
                    )
                    Divider(
                        color = colors.backgroundTertiary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 12.dp)
                            .height(1.dp)
                    )
                    Text(
                        text = stringResource(R.string.common_recipe_screen_recipe_not_found_description),
                        style = typography.body1,
                        modifier = Modifier.align(Alignment.Start),
                        color = colors.foregroundSecondary
                    )
                    Divider(
                        color = colors.backgroundTertiary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .height(1.dp)
                    )
                    DynamicButton(
                        leftIcon = ImageVector.vectorResource(R.drawable.ic_refresh),
                        text = stringResource(R.string.common_general_refresh).uppercase(),
                        isSelected = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        onClick = onRefresh,
                    )
                }
            }
        }
        BottomSheetCloseButton { coroutine.launch { sheetState.hide() } }
    }

    if (state is RecipeScreenState.Success) {
        RecipeScreenDialogs(
            state = state,
            onEvent = onEvent,
        )
    }

    LaunchedEffect(defaultTab) {
        if (defaultTab != RecipeScreenTab.Details) {
            pagerState.scrollToPage(tabs.indexOf(defaultTab))
            columnState.animateScrollToItem(3)
        }
    }
}

private val tabs =
    listOf(RecipeScreenTab.Details, RecipeScreenTab.Ingredients, RecipeScreenTab.Cooking)
