package com.mysty.chefbook.features.recipe.info.ui.components.details.info

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.api.common.entities.language.Language
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.api.recipe.domain.entities.visibility.Visibility
import com.mysty.chefbook.features.recipe.info.R
import com.mysty.chefbook.ui.common.extensions.localizedName
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
internal fun InfoWidget(
  recipe: Recipe,
  modifier: Modifier = Modifier,
) {
  val resources = LocalContext.current.resources
  LazyVerticalGrid(
    modifier = modifier,
    columns = GridCells.Fixed(2),
    userScrollEnabled = false
  ) {

    recipe.description?.let { description ->
      item(span = { GridItemSpan(2) }) {
        InfoElement(
          name = stringResource(R.string.common_general_description),
          value = description,
          modifier = Modifier.padding(bottom = 12.dp),
        )
      }
    }

    recipe.ownerName?.let { author ->
      item {
        InfoElement(
          name = stringResource(R.string.common_general_author),
          value = author,
          modifier = Modifier.padding(bottom = 12.dp),
        )
      }
    }

    if (recipe.language != Language.OTHER) {
      item {
        InfoElement(
          name = stringResource(R.string.common_general_language),
          value = recipe.language.localizedName(resources),
          modifier = Modifier.padding(bottom = 12.dp),
        )
      }
    }

    item {
      InfoElement(
        name = stringResource(R.string.common_general_visibility),
        value = when (recipe.visibility) {
          Visibility.PRIVATE -> stringResource(R.string.common_general_only_author)
          Visibility.SHARED -> stringResource(R.string.common_general_by_link)
          Visibility.PUBLIC -> stringResource(R.string.common_general_community)
        },
        modifier = Modifier.padding(bottom = 12.dp),
      )
    }

    item {
      InfoElement(
        name = stringResource(R.string.common_general_creation_date),
        value = recipe.creationTimestamp.toLocalDate()
          .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
        modifier = Modifier.padding(bottom = 12.dp),
      )
    }

    if (recipe.updateTimestamp.toLocalDate() != recipe.creationTimestamp.toLocalDate()) {
      item {
        InfoElement(
          name = stringResource(R.string.common_general_update_date),
          value = recipe.updateTimestamp.toLocalDate()
            .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
        )
      }
    }

  }
}
