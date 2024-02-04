package io.chefbook.features.recipe.info.ui.components.details.info

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.libs.models.language.Language
import io.chefbook.libs.utils.time.parseTimestampSafely
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta.Visibility
import io.chefbook.ui.common.extensions.localizedName
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import io.chefbook.core.android.R as coreR

@SuppressLint("NewApi")
@Composable
internal fun InfoWidget(
  recipe: DecryptedRecipe,
  modifier: Modifier = Modifier,
) {
  val resources = LocalContext.current.resources

  val creationTimestamp = remember(recipe.creationTimestamp) {
    parseTimestampSafely(recipe.creationTimestamp)?.toJavaLocalDateTime()
      ?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
  }
  val updateTimestamp = remember(recipe.updateTimestamp) {
    parseTimestampSafely(recipe.updateTimestamp)?.toJavaLocalDateTime()
      ?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
  }

  LazyVerticalGrid(
    modifier = modifier,
    columns = GridCells.Fixed(2),
    userScrollEnabled = false
  ) {

    recipe.description?.let { description ->
      item(span = { GridItemSpan(2) }) {
        InfoElement(
          name = stringResource(coreR.string.common_general_description),
          value = description,
          modifier = Modifier.padding(bottom = 12.dp),
        )
      }
    }

    recipe.owner.name?.let { author ->
      item {
        InfoElement(
          name = stringResource(coreR.string.common_general_author),
          value = author,
          modifier = Modifier.padding(bottom = 12.dp),
        )
      }
    }

    if (recipe.language != Language.OTHER) {
      item {
        InfoElement(
          name = stringResource(coreR.string.common_general_language),
          value = recipe.language.localizedName(resources),
          modifier = Modifier.padding(bottom = 12.dp),
        )
      }
    }

    item {
      InfoElement(
        name = stringResource(coreR.string.common_general_visibility),
        value = when (recipe.visibility) {
          Visibility.PRIVATE -> stringResource(coreR.string.common_general_only_author)
          Visibility.LINK -> stringResource(coreR.string.common_general_by_link)
          Visibility.PUBLIC -> stringResource(coreR.string.common_general_community)
        },
        modifier = Modifier.padding(bottom = 12.dp),
      )
    }

    creationTimestamp?.let {
      item {
        InfoElement(
          name = stringResource(coreR.string.common_general_creation_date),
          value = creationTimestamp,
          modifier = Modifier.padding(bottom = 12.dp),
        )
      }
    }

    if (updateTimestamp != creationTimestamp && updateTimestamp != null) {
      item {
        InfoElement(
          name = stringResource(coreR.string.common_general_update_date),
          value = updateTimestamp,
        )
      }
    }
  }
}
