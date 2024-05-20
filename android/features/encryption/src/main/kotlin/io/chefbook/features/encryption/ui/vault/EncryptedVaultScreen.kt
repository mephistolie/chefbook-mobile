package io.chefbook.features.encryption.ui.vault

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.core.android.showToast
import io.chefbook.features.encryption.ui.vault.mvi.EncryptedVaultScreenEffect
import io.chefbook.navigation.navigators.BaseNavigator
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Destination(route = "encryption/vault", style = DestinationStyleBottomSheet::class)
@Composable
internal fun EncryptedVaultScreen(
  closeOnUnlocked: Boolean = false,
  navigator: BaseNavigator,
) {
  val viewModel =
    koinViewModel<EncryptedVaultScreenViewModel> { parametersOf(closeOnUnlocked) }
  val state = viewModel.state.collectAsStateWithLifecycle()

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
