package io.chefbook.features.recipebook.dashboard.ui.components.blocks

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.features.recipebook.dashboard.R
import io.chefbook.features.recipebook.dashboard.ui.components.elements.RecipeBookActionButton
import io.chefbook.sdk.encryption.vault.api.external.domain.entities.EncryptedVaultState
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@Composable
internal fun RecipeBookOnlineBlock(
  encryption: EncryptedVaultState,
  onCommunityRecipesButtonClick: () -> Unit,
  onEncryptedVaultButtonClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
  ) {
    RecipeBookActionButton(
      title = stringResource(id = R.string.common_dashboard_screen_community_recipes),
      hint = stringResource(id = coreR.string.common_general_open),
      image = painterResource(id = designR.drawable.ic_compass_illustration),
      onActionButtonClick = { onCommunityRecipesButtonClick() },
      modifier = Modifier
        .weight(1f)
        .aspectRatio(2f),
      imageModifier = Modifier
        .requiredHeight(112.dp)
        .offset(28.dp, 12.dp),
    )
    Spacer(modifier = Modifier.width(8.dp))
    RecipeBookActionButton(
      title = stringResource(id = coreR.string.common_global_encrypted_vault),
      subtitle = getEncryptionStateTitle(encryption),
      hint = stringResource(id = coreR.string.common_general_manage),
      image = painterResource(id = designR.drawable.ic_lock_illustration),
      onActionButtonClick = { onEncryptedVaultButtonClick() },
      modifier = Modifier
        .weight(1f)
        .aspectRatio(2f),
      imageModifier = Modifier
        .requiredHeight(112.dp)
        .offset(28.dp, 8.dp),
    )
  }
}

@Composable
private fun getEncryptionStateTitle(state: EncryptedVaultState) =
  when (state) {
    EncryptedVaultState.DISABLED -> stringResource(coreR.string.common_general_disabled)
    EncryptedVaultState.LOCKED -> stringResource(coreR.string.common_general_locked)
    EncryptedVaultState.UNLOCKED -> stringResource(coreR.string.common_general_unlocked)
    EncryptedVaultState.LOADING -> ""
  }
