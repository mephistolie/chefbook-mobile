package io.chefbook.design.components.spacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
@NonRestartableComposable
fun VerticalSpacer(height: Dp) = Spacer(modifier = Modifier.height(height))
