package com.mysty.chefbook.features.encryption.ui.vault

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.mysty.chefbook.core.android.showToast
import com.mysty.chefbook.features.encryption.ui.vault.mvi.EncryptedVaultScreenEffect
import com.mysty.chefbook.navigation.navigators.IBaseNavigator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyle
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Destination(route = "encryption/vault", style = DestinationStyle.BottomSheet::class)
@Composable
internal fun EncryptedVaultScreen(
    closeOnUnlocked: Boolean = false,
    navigator: IBaseNavigator,
) {
    val viewModel: IEncryptedVaultScreenViewModel =
        getViewModel<EncryptedVaultScreenViewModel> { parametersOf(closeOnUnlocked) }
    val state = viewModel.state.collectAsState()

    val context = LocalContext.current

    EncryptedVaultScreenContent(
        state = state.value,
        onIntent = viewModel::handleIntent,
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is EncryptedVaultScreenEffect.ShowToast -> context.showToast(effect.messageId)
                is EncryptedVaultScreenEffect.Close -> navigator.navigateUp()
            }
        }
    }
}
