package io.chefbook.features.encryption.ui.vault

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import io.chefbook.features.encryption.ui.vault.mvi.EncryptedVaultScreenEffect
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.core.android.showToast
import io.chefbook.navigation.navigators.BaseNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Destination(route = "encryption/vault", style = DestinationStyleBottomSheet::class)
@Composable
internal fun EncryptedVaultScreen(
  closeOnUnlocked: Boolean = false,
  navigator: BaseNavigator,
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
