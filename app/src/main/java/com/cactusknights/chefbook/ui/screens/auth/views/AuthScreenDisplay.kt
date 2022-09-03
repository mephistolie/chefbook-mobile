package com.cactusknights.chefbook.ui.screens.auth.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.core.auth.AuthUtils
import com.cactusknights.chefbook.core.auth.Password
import com.mephistolie.compost.modifiers.simpleClickable
import com.cactusknights.chefbook.ui.screens.auth.models.AuthAction
import com.cactusknights.chefbook.ui.screens.auth.models.AuthProgress
import com.cactusknights.chefbook.ui.screens.auth.models.AuthScreenEvent
import com.cactusknights.chefbook.ui.screens.auth.models.AuthScreenState
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.themes.DeepOrangeDark
import com.cactusknights.chefbook.ui.themes.DeepOrangeLight
import com.cactusknights.chefbook.ui.views.buttons.CircleImageButton
import com.cactusknights.chefbook.ui.views.buttons.RoundedTextButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthScreenDisplay(
    authState: AuthScreenState,
    onEvent: (AuthScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    val emailText = remember { mutableStateOf("") }
    val passwordText = remember { mutableStateOf("") }
    val repeatPasswordText = remember { mutableStateOf("") }

    val emailCheck = AuthUtils.validateEmail(emailText.value)
    val passwordCheck = AuthUtils.validatePassword(passwordText.value, repeatPasswordText.value)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ChefBookLogo()
        Spacer(Modifier.height(16.dp))
        EmailInputField(
            text = emailText,
            readOnly = authState.progress == AuthProgress.LOADING,
            imeAction = if (authState.action != AuthAction.RESET_PASSWORD) ImeAction.Next else ImeAction.Done
        )
        AnimatedVisibility(visible = authState.action != AuthAction.RESET_PASSWORD) {
            PasswordInputField(
                text = passwordText,
                modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp),
                readOnly = authState.progress == AuthProgress.LOADING,
                imeAction = if (authState.action == AuthAction.SIGN_UP) ImeAction.Next else ImeAction.Done
            )
        }
        AnimatedVisibility(visible = authState.action == AuthAction.SIGN_UP) {
            Spacer(Modifier.height(8.dp))
            PasswordInputField(
                text = repeatPasswordText,
                modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp),
                readOnly = authState.progress == AuthProgress.LOADING,
                hint = stringResource(id = R.string.common_auth_screen_repeat_password)
            )
        }
        Spacer(Modifier.height(16.dp))
        AnimatedVisibility(
            visible = emailText.value.isNotEmpty() && !emailCheck || authState.action == AuthAction.SIGN_UP &&
                    passwordText.value.isNotEmpty() && passwordCheck != Password.VALID
        ) {
            Row(
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_info),
                    contentDescription = "info",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (emailText.value.isNotEmpty() && !emailCheck) stringResource(id = R.string.common_auth_screen_invalid_email)
                    else {
                        when (passwordCheck) {
                            Password.SHORT -> stringResource(id = R.string.common_auth_screen_short_password)
                            Password.UPPER -> stringResource(id = R.string.common_auth_screen_upper_password)
                            Password.LOWER -> stringResource(id = R.string.common_auth_screen_lower_password)
                            Password.NUMBER -> stringResource(id = R.string.common_auth_screen_number_password)
                            Password.SPACE -> stringResource(id = R.string.common_auth_screen_space_password)
                            else -> stringResource(id = R.string.common_auth_screen_password_mismatch)
                        }
                    },
                    style = typography.headline1,
                    color = colors.foregroundPrimary
                )
            }
        }
        AnimatedVisibility(visible = authState.progress != AuthProgress.LOADING) {
            RoundedTextButton(
                text = when (authState.action) {
                    AuthAction.SIGN_IN -> stringResource(id = R.string.common_auth_screen_sign_in)
                    AuthAction.SIGN_UP -> stringResource(id = R.string.common_auth_screen_sign_up)
                    AuthAction.RESET_PASSWORD -> stringResource(id = R.string.common_auth_screen_reset_password)
                }, onClick = {
                    keyboardController?.hide()
                    when (authState.action) {
                        AuthAction.SIGN_IN -> onEvent(
                            AuthScreenEvent.SignIn(
                                email = emailText.value,
                                password = passwordText.value,
                            )
                        )
                        AuthAction.SIGN_UP -> onEvent(
                            AuthScreenEvent.SignUp(
                                email = emailText.value,
                                password = passwordText.value,
                            )
                        )
                        AuthAction.RESET_PASSWORD -> onEvent(
                            AuthScreenEvent.ResetPassword(
                                email = emailText.value
                            )
                        )
                    }
                },
                enabled = when (authState.action) {
                    AuthAction.SIGN_IN -> emailCheck && passwordText.value.isNotEmpty()
                    AuthAction.SIGN_UP -> emailCheck && passwordCheck == Password.VALID
                    AuthAction.RESET_PASSWORD -> emailCheck
                },
                textColor = DeepOrangeDark,
                color = colors.tintPrimary,
                modifier = Modifier.fillMaxWidth()
            )
        }
        AnimatedVisibility(visible = authState.progress == AuthProgress.LOADING) {
            CircularProgressIndicator(
                color = colors.tintPrimary,
                modifier = Modifier.size(36.dp)
            )
        }

        AnimatedVisibility(visible = authState.action == AuthAction.SIGN_IN && authState.progress != AuthProgress.LOADING) {
            Column(
                modifier = Modifier.wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(16.dp))
                Divider(
                    color = colors.backgroundTertiary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(96.dp, 0.dp)
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.height(48.dp)
                ) {
                    CircleImageButton(
                        image = ImageVector.vectorResource(R.drawable.ic_google),
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .size(48.dp)
                            .padding(5.dp)
                    )
                    CircleImageButton(
                        image = ImageVector.vectorResource(R.drawable.ic_vk),
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .size(48.dp)
                            .padding(5.dp)
                    )
                    CircleImageButton(
                        image = ImageVector.vectorResource(R.drawable.ic_disable_sync),
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .size(48.dp)
                            .padding(5.dp)
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(visible = authState.action != AuthAction.RESET_PASSWORD) {
            Text(
                text = stringResource(id = R.string.common_auth_screen_forgot_password),
                modifier = Modifier.simpleClickable {
                    focusManager.clearFocus(force = true)
                    onEvent(AuthScreenEvent.OpenPasswordResetScreen)
                },
                style = ChefBookTheme.typography.body2,
                color = DeepOrangeLight
            )
        }
        Spacer(Modifier.height(8.dp))
        Row {
            Text(
                text = if (authState.action != AuthAction.SIGN_UP) stringResource(id = R.string.common_auth_screen_not_member) else stringResource(
                    id = R.string.common_auth_screen_already_member
                ),
                style = ChefBookTheme.typography.body2,
                color = ChefBookTheme.colors.foregroundSecondary
            )
            Text(
                text = if (authState.action == AuthAction.SIGN_IN) stringResource(id = R.string.common_auth_screen_sign_up) else stringResource(
                    id = R.string.common_auth_screen_sign_in
                ),
                modifier = Modifier.simpleClickable {
                    focusManager.clearFocus(force = true)
                    if (authState.action == AuthAction.SIGN_IN)
                        onEvent(AuthScreenEvent.OpenSignUpScreen)
                    else
                        onEvent(AuthScreenEvent.OpenSignInScreen)
                },
                style = ChefBookTheme.typography.body2,
                color = DeepOrangeLight
            )
        }
    }

    if (authState.progress == AuthProgress.ERROR) {
        AuthErrorDialog(
            error = authState.error,
            onHide = {
                passwordText.value = ""
                repeatPasswordText.value = ""
                onEvent(AuthScreenEvent.CloseDialog)
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewLightSignInScreen() {
    ThemedAuthScreen(AuthScreenState(AuthAction.SIGN_IN), false)
}

@Composable
@Preview(showBackground = true)
fun PreviewDarkSignUpScreen() {
    ThemedAuthScreen(
        AuthScreenState(action = AuthAction.SIGN_IN, progress = AuthProgress.LOADING),
        true
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewDarkResetPasswordScreen() {
    ThemedAuthScreen(AuthScreenState(AuthAction.RESET_PASSWORD), false)
}

@Composable
private fun ThemedAuthScreen(
    viewState: AuthScreenState,
    isDarkTheme: Boolean
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = ChefBookTheme.colors.backgroundPrimary
        ) {
            AuthScreenDisplay(
                authState = viewState,
                onEvent = {},
            )
        }
    }
}