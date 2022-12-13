package com.cactusknights.chefbook.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.cactusknights.chefbook.ui.navigation.Destination
import com.cactusknights.chefbook.ui.screens.auth.models.AuthScreenEffect
import com.cactusknights.chefbook.ui.screens.auth.views.AuthScreenDisplay
import org.koin.androidx.compose.getViewModel

@Composable
fun AuthScreen(
    appNavController: NavController,
    authViewModel: AuthViewModel = getViewModel()
) {
    val authState = authViewModel.authState.collectAsState()

    AuthScreenDisplay(
        authState = authState.value,
        onEvent = authViewModel::obtainEvent
    )

    LaunchedEffect(Unit) {
        authViewModel.effect.collect { effect ->
            if (effect is AuthScreenEffect.SignedIn) {
                appNavController.navigate(Destination.Home.route) {
                    popUpTo(Destination.Home.route) { inclusive = true }
                }
            }
        }
    }
}
