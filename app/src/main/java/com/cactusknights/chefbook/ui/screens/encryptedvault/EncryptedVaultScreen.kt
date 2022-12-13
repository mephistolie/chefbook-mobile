package com.cactusknights.chefbook.ui.screens.encryptedvault

import android.widget.Toast
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.cactusknights.chefbook.ui.screens.encryptedvault.models.EncryptedVaultScreenEffect
import com.cactusknights.chefbook.ui.screens.encryptedvault.models.EncryptedVaultScreenEvent
import com.cactusknights.chefbook.ui.screens.encryptedvault.views.EncryptedVaultScreenDisplay
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EncryptedVaultScreen(
    sheetState: ModalBottomSheetState,
    viewModel: EncryptedVaultScreenViewModel = getViewModel(),
    closeOnUnlocked: Boolean = false,
) {
    if (closeOnUnlocked) viewModel.obtainEvent(EncryptedVaultScreenEvent.CloseOnUnlocked)

    val context = LocalContext.current
    val resources = context.resources

    val state = viewModel.state.collectAsState()

    EncryptedVaultScreenDisplay(
        state = state.value,
        onEvent = viewModel::obtainEvent,
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is EncryptedVaultScreenEffect.Toast -> {
                    Toast.makeText(context, resources.getString(effect.messageId), Toast.LENGTH_SHORT).show()
                }
                is EncryptedVaultScreenEffect.OnClose -> {
                    sheetState.hide()
                }
            }
        }
    }
}
