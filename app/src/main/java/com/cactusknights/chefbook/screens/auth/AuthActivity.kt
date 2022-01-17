package com.cactusknights.chefbook.screens.auth

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.screens.main.MainActivity
import com.cactusknights.chefbook.databinding.ActivityAuthBinding
import com.cactusknights.chefbook.common.showToast
import com.cactusknights.chefbook.screens.auth.models.AuthViewEffect
import com.cactusknights.chefbook.screens.auth.models.AuthEvent
import com.cactusknights.chefbook.screens.auth.models.AuthScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class AuthActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            when (viewModel.authState.value) {
                is AuthScreenState.SignInScreen -> {
                    viewModel.obtainEvent(
                        AuthEvent.SignIn(
                            binding.inputEmail.text.toString(),
                            binding.inputPassword.text.toString()))
                }
                is AuthScreenState.SignUpScreen -> {
                    viewModel.obtainEvent(
                        AuthEvent.SignUp(
                            binding.inputEmail.text.toString(),
                            binding.inputPassword.text.toString(),
                            binding.inputRepeatPassword.text.toString()))
                }
                is AuthScreenState.RestorePasswordScreen -> { /* TODO */ }
            }
        }

        binding.btnLocal.setOnClickListener { viewModel.obtainEvent(AuthEvent.SignInLocally) }

        binding.textSignUp.setOnClickListener {
            if (viewModel.authState.value is AuthScreenState.SignInScreen) { viewModel.obtainEvent(AuthEvent.SignUpScreen) }
            else { viewModel.obtainEvent(AuthEvent.SignInSelected) }
        }

        binding.btnResetPassword.setOnClickListener {
            viewModel.obtainEvent(AuthEvent.RestorePasswordScreen)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.authState.collect { render(it) } }
                launch { viewModel.viewEffect.collect { handleViewEffect(it) } }
            }
        }
    }

    private fun render(state: AuthScreenState) {
        when (state) {
            is AuthScreenState.SignInScreen -> setSignInLayout()
            is AuthScreenState.SignUpScreen -> setSignUpLayout()
            is AuthScreenState.RestorePasswordScreen -> setRestoreLayout()
        }
        updateLoginProgressLayout(state.isLoading)
    }

    private fun handleViewEffect(effect: AuthViewEffect) {
        when (effect) {
            is AuthViewEffect.Message -> applicationContext.showToast(effect.messageId)
            is AuthViewEffect.SignedIn -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent) }
        }
    }

    private fun setSignUpLayout() {

        binding.ilPassword.visibility = View.VISIBLE
        binding.ilRepeatPassword.visibility = View.VISIBLE
        binding.viewSeparator.visibility = View.GONE
        binding.llProviders.visibility = View.GONE

        binding.btnLogin.text = resources.getText(R.string.signup)
        binding.textMembership.text = resources.getText(R.string.already_member)
        binding.textSignUp.text = resources.getText(R.string.login)
    }

    private fun setSignInLayout() {

        binding.ilPassword.visibility = View.VISIBLE
        binding.ilRepeatPassword.visibility = View.GONE
        binding.viewSeparator.visibility = View.VISIBLE
        binding.llProviders.visibility = View.VISIBLE

        binding.textMembership.text = resources.getText(R.string.not_member)
        binding.textSignUp.text = resources.getText(R.string.signup)
        binding.btnLogin.text = resources.getText(R.string.login)
    }

    private fun setRestoreLayout() {

        binding.ilPassword.visibility = View.GONE
        binding.ilRepeatPassword.visibility = View.GONE
        binding.viewSeparator.visibility = View.GONE
        binding.llProviders.visibility = View.GONE

        binding.btnLogin.text = resources.getText(R.string.reset_password)
        binding.textMembership.text = resources.getText(R.string.already_member)
        binding.textSignUp.text = resources.getText(R.string.login)
    }

    private fun updateLoginProgressLayout(isLoading: Boolean) {
        binding.btnLogin.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}