package com.cactusknights.chefbook.screens.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.showToast
import com.cactusknights.chefbook.databinding.ActivityAuthBinding
import com.cactusknights.chefbook.screens.auth.models.AuthScreenEvent
import com.cactusknights.chefbook.screens.auth.models.AuthScreenState
import com.cactusknights.chefbook.screens.auth.models.AuthScreenViewEffect
import com.cactusknights.chefbook.screens.main.MainActivity
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
                        AuthScreenEvent.SignIn(
                            binding.inputEmail.text.toString(),
                            binding.inputPassword.text.toString()))
                }
                is AuthScreenState.SignUpScreen -> {
                    viewModel.obtainEvent(
                        AuthScreenEvent.SignUp(
                            binding.inputEmail.text.toString(),
                            binding.inputPassword.text.toString(),
                            binding.inputRepeatPassword.text.toString()))
                }
                is AuthScreenState.RestorePasswordScreen -> { /* TODO */ }
            }
        }

        binding.btnLocal.setOnClickListener { viewModel.obtainEvent(AuthScreenEvent.SignInLocally) }

        binding.textSignUp.setOnClickListener {
            if (viewModel.authState.value is AuthScreenState.SignInScreen) { viewModel.obtainEvent(AuthScreenEvent.SignUpScreen) }
            else { viewModel.obtainEvent(AuthScreenEvent.SignInSelected) }
        }

        binding.btnResetPassword.setOnClickListener {
            viewModel.obtainEvent(AuthScreenEvent.RestorePasswordScreen)
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

    private fun handleViewEffect(effect: AuthScreenViewEffect) {
        when (effect) {
            is AuthScreenViewEffect.Message -> applicationContext.showToast(effect.messageId)
            is AuthScreenViewEffect.SignedIn -> {
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