package com.cactusknights.chefbook.ui.screens.recipe.views.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.domain.entities.recipe.cooking.CookingItem
import com.cactusknights.chefbook.ui.screens.recipe.views.elements.CookingStep
import com.cactusknights.chefbook.ui.screens.recipe.views.elements.Section
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme

@Composable
fun CookingPage(
    cooking: List<CookingItem>,
    onStepPictureClicked: (String) -> Unit,
) {
    val colors = LocalTheme.colors

    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        var stepNumber = 1
        cooking.forEachIndexed { index, item ->
            when (item) {
                is CookingItem.Section -> {
                    Section(
                        name = item.name,
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                    )
                }
                is CookingItem.Step -> {
                    CookingStep(
                        number = stepNumber,
                        step = item,
                        onStepPictureClicked = onStepPictureClicked,
                        modifier = Modifier
                            .padding(bottom = 4.dp),
                    )
                    if (index + 1 < cooking.size && cooking[index + 1] is CookingItem.Section) {
                        Divider(
                            color = colors.backgroundSecondary,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                                .height(1.dp)
                        )
                    }
                    stepNumber += 1
                }
                else -> Unit
            }
        }
        Spacer(modifier = Modifier.navigationBarsPadding().height(32.dp))
    }
}
