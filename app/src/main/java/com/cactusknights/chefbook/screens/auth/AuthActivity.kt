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
import com.cactusknights.chefbook.legacy.helpers.showToast
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

        val chefbookTitle = SpannableString("ChefBook")
        chefbookTitle.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.deep_orange)), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        chefbookTitle.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.monochrome_invert)), 4, chefbookTitle.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textAppName.text = chefbookTitle

        binding.btnLogin.setOnClickListener {
            when(viewModel.state.value.authState) {
                SignStates.SIGN_IN -> { viewModel.signIn(binding.inputEmail.text.toString(), binding.inputPassword.text.toString()) }
                SignStates.SIGN_UP -> { viewModel.signUp(binding.inputEmail.text.toString(), binding.inputPassword.text.toString(), binding.inputRepeatPassword.text.toString()) }
                else -> { viewModel.updateState(AuthActivityState(
                    authState = SignStates.RESTORE_PASSWORD,
                    inProgress = viewModel.state.value.inProgress))
                }
            }
        }

        binding.textSignUp.setOnClickListener {
            if (viewModel.state.value.authState == SignStates.SIGN_IN) { viewModel.updateState(AuthActivityState(
                    authState = SignStates.SIGN_UP,
                    inProgress = viewModel.state.value.inProgress))
            } else {
                viewModel.updateState(AuthActivityState(
                    authState = SignStates.SIGN_IN,
                    inProgress = viewModel.state.value.inProgress))
            }
        }

        binding.btnResetPassword.setOnClickListener {
            setRestoreLayout()
            viewModel.updateState(AuthActivityState(
                authState = SignStates.RESTORE_PASSWORD,
                inProgress = viewModel.state.value.inProgress))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { currentState ->
                    val currentMessage = viewModel.state.value.message
                    if (currentMessage is String) applicationContext.showToast(currentMessage)
                    else if (currentMessage is Int) applicationContext.showToast(currentMessage)
                    when (currentState.authState) {
                        SignStates.SIGN_UP -> setSignUpLayout()
                        SignStates.SIGN_IN -> setSignInLayout()
                        SignStates.RESTORE_PASSWORD -> setRestoreLayout()
                        else -> {
                            val intent = Intent(this@AuthActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    updateLoginProgressLayout()
                }
            }
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

    private fun updateLoginProgressLayout() {
        binding.btnLogin.visibility = if (viewModel.state.value.inProgress) View.GONE else View.VISIBLE
        binding.progressLogin.visibility = if (viewModel.state.value.inProgress) View.VISIBLE else View.GONE
    }
}