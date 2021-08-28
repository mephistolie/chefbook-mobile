package com.cactusknights.chefbook.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.ActivityLoginBinding
import com.cactusknights.chefbook.enums.LoginStates
import com.cactusknights.chefbook.helpers.Utils
import com.cactusknights.chefbook.helpers.Utils.hideKeyboard
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.viewmodels.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


open class LoginActivity : AppCompatActivity() {

    private var state = LoginStates.LOGIN
    private lateinit var userViewModel: UserViewModel

    private lateinit var binding: ActivityLoginBinding

    private val googleLogInContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
        if (result?.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                userViewModel.logonGoogle(account.idToken!!, ::onLogInCallback)
            } catch (e: ApiException) {}
        } else {
            Toast.makeText(this, resources.getString(R.string.failed_to_google), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chefbookTitle = SpannableString("ChefBook")
        chefbookTitle.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.deep_orange)), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        chefbookTitle.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.monochrome_invert)), 4, chefbookTitle.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textAppName.text = chefbookTitle

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.logout()

        binding.btnLogin.setOnClickListener {
            when(state) {
                LoginStates.LOGIN -> { login() }
                LoginStates.SIGNUP -> { signUp() }
                else -> { restorePassword() }
            }
        }

        binding.btnGoogle.setOnClickListener {
            logonGoogle()
        }

        binding.textSignUp.setOnClickListener {
            if (state == LoginStates.LOGIN) {
                state = LoginStates.SIGNUP
                setSignUpLayout()
            } else {
                state = LoginStates.LOGIN
                setLoginLayout()
            }
        }

        binding.btnResetPassword.setOnClickListener {
            setRestoreLayout()
            state = LoginStates.RESTORE
        }
    }

    override fun onStart() {
        lifecycleScope.launch { listenToUser() }
        super.onStart()
    }

    private suspend fun listenToUser() {
        userViewModel.listenToUser().collect { user: User? ->
            if (user != null) {
                Toast.makeText(this, R.string.login_successfully, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setSignUpLayout() {

        binding.inputPassword.setText("")
        binding.inputRepeatPassword.setText("")

        binding.ilPassword.visibility = View.VISIBLE
        binding.ilRepeatPassword.visibility = View.VISIBLE
        binding.viewSeparator.visibility = View.GONE
        binding.llProviders.visibility = View.GONE

        binding.btnLogin.text = resources.getText(R.string.signup)
        binding.textMembership.text = resources.getText(R.string.already_member)
        binding.textSignUp.text = resources.getText(R.string.login)
    }

    private fun setLoginLayout() {

        binding.inputPassword.text = null

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

        state = LoginStates.RESTORE
    }

    private fun changeLoginProgressLayout() {
        binding.btnLogin.visibility = if (binding.btnLogin.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        binding.progressLogin.visibility = if (binding.progressLogin.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    private fun login() {
        val emailText = binding.inputEmail.text.toString()
        val passwordText = binding.inputPassword.text.toString()
        if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
            userViewModel.logonEmail(binding.inputEmail.text.toString(), binding.inputPassword.text.toString(), ::onLogInCallback)
            changeLoginProgressLayout()
        } else {
            Toast.makeText(this, R.string.empty_fields, Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUp() {
        val emailText = binding.inputEmail.text.toString()
        val passwordText = binding.inputPassword.text.toString()
        val repeatPasswordText = binding.inputRepeatPassword.text.toString()
        if (Utils.checkAuthFields(emailText, passwordText, repeatPasswordText, this)) {
            userViewModel.signup(emailText, passwordText, ::onSignUpCallback)
            changeLoginProgressLayout()
        }
    }

    private fun restorePassword() {
        val emailText = binding.inputEmail.text.toString()
        if (!emailText.contains('@') || !emailText.contains('.')) {
            Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show()
        } else {
            userViewModel.restorePassword(emailText, ::onRestorePasswordCallback)
            changeLoginProgressLayout()
            hideKeyboard(this)
        }
    }

    private fun logonGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        googleLogInContract.launch(signInIntent)
    }

    private fun onLogInCallback(isDeleted: Boolean) {
        if (isDeleted) { Toast.makeText(applicationContext, R.string.login_successfully, Toast.LENGTH_SHORT).show() }
        else {
            Toast.makeText(applicationContext, R.string.authentication_failed, Toast.LENGTH_SHORT).show()
            changeLoginProgressLayout()
        }
    }

    private fun onSignUpCallback(isDeleted: Boolean) {
        if (isDeleted) { Toast.makeText(applicationContext, R.string.signup_successfully, Toast.LENGTH_SHORT).show() }
        else {
            Toast.makeText(applicationContext, R.string.signup_failed, Toast.LENGTH_SHORT).show()
            changeLoginProgressLayout()
        }

    }

    private fun onRestorePasswordCallback(isDeleted: Boolean) {
        if (isDeleted) { Toast.makeText(applicationContext, R.string.reset_password_successfully, Toast.LENGTH_SHORT).show() }
        else {
            Toast.makeText(applicationContext, R.string.reset_password_failed, Toast.LENGTH_SHORT).show()
            changeLoginProgressLayout()
        }
    }
}