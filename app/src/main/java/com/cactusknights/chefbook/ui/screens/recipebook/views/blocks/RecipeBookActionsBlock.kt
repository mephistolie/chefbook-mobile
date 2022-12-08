package com.cactusknights.chefbook.ui.screens.recipebook.views.blocks

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
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.entities.encryption.EncryptedVaultState
import com.cactusknights.chefbook.ui.screens.recipebook.views.elements.RecipeBookActionButton

@Composable
fun RecipeBookActionsBlock(
    encryption: EncryptedVaultState,
    onCommunityRecipesButtonClick: () -> Unit,
    onEncryptedVaultButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        RecipeBookActionButton(
            title = stringResource(id = R.string.common_recipe_book_screen_community_recipes),
            hint = stringResource(id = R.string.common_general_open),
            image = painterResource(id = R.drawable.ic_compass_illustration),
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
            title = stringResource(id = R.string.common_global_encrypted_vault),
            subtitle = getEncryptionStateTitle(encryption),
            hint = stringResource(id = R.string.common_general_manage),
            image = painterResource(id = R.drawable.ic_lock_illustration),
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
        is EncryptedVaultState.Disabled -> stringResource(R.string.common_general_disabled)
        is EncryptedVaultState.Locked -> stringResource(R.string.common_general_locked)
        is EncryptedVaultState.Unlocked -> stringResource(R.string.common_general_unlocked)
    }
