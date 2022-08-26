package com.cactusknights.chefbook.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cactusknights.chefbook.ui.navigation.Destination
import com.cactusknights.chefbook.ui.screens.auth.models.AuthScreenEffect
import com.cactusknights.chefbook.ui.screens.auth.views.AuthScreenDisplay

@Composable
fun AuthScreen(
    appNavController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
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
