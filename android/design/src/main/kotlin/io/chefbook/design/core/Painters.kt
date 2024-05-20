package io.chefbook.design.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.res.painterResource
import io.chefbook.design.R

@Composable
@NonRestartableComposable
fun scorePainter(score: Int) = painterResource(
  when (score) {
    1 -> R.drawable.ic_broccy_crossed_out_eyes
    2 -> R.drawable.ic_broccy_expressionless
    3 -> R.drawable.ic_broccy_neutral
    4 -> R.drawable.ic_broccy
    else -> R.drawable.ic_broccy_stars
  }
)

@Composable
@NonRestartableComposable
fun scorePainter(rating: Float, votes: Int) = painterResource(
  when {
    votes == 0 -> R.drawable.ic_broccy_think
    rating >= 4.5 -> R.drawable.ic_broccy_stars
    rating >= 3.5 -> R.drawable.ic_broccy
    rating >= 2.5 -> R.drawable.ic_broccy_neutral
    rating >= 1.5 -> R.drawable.ic_broccy_expressionless
    else -> R.drawable.ic_broccy_crossed_out_eyes
  }
)
