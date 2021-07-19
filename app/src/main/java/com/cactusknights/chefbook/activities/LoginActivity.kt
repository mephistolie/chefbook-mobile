package com.cactusknights.chefbook.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.enums.LoginStates
import com.cactusknights.chefbook.helpers.Utils
import com.cactusknights.chefbook.helpers.Utils.hideKeyboard
import com.cactusknights.chefbook.viewmodels.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


open class LoginActivity : AppCompatActivity() {

    private var state = LoginStates.LOGIN
    private lateinit var userViewModel: UserViewModel

    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var passwordField: TextInputLayout
    private lateinit var repeatPassword: TextInputEditText
    private lateinit var repeatPasswordField: TextInputLayout

    private lateinit var loginEmailBtn: Button
    private lateinit var loginGoogleBtn: FloatingActionButton
    private lateinit var resetPasswordBtn: TextView
    private lateinit var signupBtn: TextView

    private lateinit var separator: View
    private lateinit var providers: LinearLayoutCompat
    private lateinit var membership: TextView

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
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        passwordField = findViewById(R.id.password_field)
        repeatPassword = findViewById(R.id.repeat_password)
        repeatPasswordField = findViewById(R.id.repeat_password_field)
        loginEmailBtn = findViewById(R.id.login_email)
        loginGoogleBtn = findViewById(R.id.login_google)
        resetPasswordBtn = findViewById(R.id.forgot_password)
        signupBtn = findViewById(R.id.signup)
        separator = findViewById(R.id.separator)
        providers = findViewById(R.id.providers)
        membership = findViewById(R.id.membership)

        val appName = findViewById<TextView>(R.id.app_name)
        val chefbookTitle = SpannableString("ChefBook")
        chefbookTitle.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.deep_orange_light)), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        chefbookTitle.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.monochrome_invert)), 4, chefbookTitle.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        appName.text = chefbookTitle

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.logout()

        loginEmailBtn.setOnClickListener {
            when(state) {
                LoginStates.LOGIN -> userViewModel.logonEmail(email.text.toString(), password.text.toString(), ::onLogInCallback)
                LoginStates.SIGNUP -> signUp()
                else -> {
                    userViewModel.restorePassword(email.text.toString(), ::onRestorePasswordCallback)
                    hideKeyboard(this)
                }
            }
        }

        loginGoogleBtn.setOnClickListener {
            logonGoogle()
        }

        signupBtn.setOnClickListener {
            if (state == LoginStates.LOGIN) {
                state = LoginStates.SIGNUP
                setLoginLayout()
            } else {
                state = LoginStates.LOGIN
                setSignupLayout()
            }
        }

        resetPasswordBtn.setOnClickListener {
            setRestoreLayout()
            state = LoginStates.RESTORE
        }
    }

    override fun onStart() {
        super.onStart()
        userViewModel.getCurrentUser().observe(this,  {
            user -> if (user != null) {
                Toast.makeText(this, R.string.login_successfully, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun setLoginLayout() {

        password.setText("")
        repeatPassword.setText("")

        passwordField.visibility = View.VISIBLE
        repeatPasswordField.visibility = View.VISIBLE
        separator.visibility = View.GONE
        providers.visibility = View.GONE

        loginEmailBtn.text = resources.getText(R.string.signup)
        membership.text = resources.getText(R.string.already_member)
        signupBtn.text = resources.getText(R.string.login)
    }

    private fun setSignupLayout() {

        password.text = null

        passwordField.visibility = View.VISIBLE
        repeatPasswordField.visibility = View.GONE
        separator.visibility = View.VISIBLE
        providers.visibility = View.VISIBLE

        membership.text = resources.getText(R.string.not_member)
        signupBtn.text = resources.getText(R.string.signup)
        loginEmailBtn.text = resources.getText(R.string.login)
    }

    private fun setRestoreLayout() {

        passwordField.visibility = View.GONE
        repeatPasswordField.visibility = View.GONE
        separator.visibility = View.GONE
        providers.visibility = View.GONE

        loginEmailBtn.text = resources.getText(R.string.reset_password)
        membership.text = resources.getText(R.string.already_member)
        signupBtn.text = resources.getText(R.string.login)

        state = LoginStates.RESTORE
    }

    private fun signUp() {
        val emailText = email.text.toString()
        val passwordText = password.text.toString()
        val repeatPasswordText = repeatPassword.text.toString()
        if (Utils.checkAuthFields(emailText, passwordText, repeatPasswordText, this))
            userViewModel.signup(emailText, passwordText, ::onSignUpCallback)
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
        else { Toast.makeText(applicationContext, R.string.authentication_failed, Toast.LENGTH_SHORT).show() }
    }

    private fun onSignUpCallback(isDeleted: Boolean) {
        if (isDeleted) { Toast.makeText(applicationContext, R.string.signup_successfully, Toast.LENGTH_SHORT).show() }
        else { Toast.makeText(applicationContext, R.string.signup_failed, Toast.LENGTH_SHORT).show() }
    }

    private fun onRestorePasswordCallback(isDeleted: Boolean) {
        if (isDeleted) { Toast.makeText(applicationContext, R.string.reset_password_successfully, Toast.LENGTH_SHORT).show() }
        else { Toast.makeText(applicationContext, R.string.reset_password_failed, Toast.LENGTH_SHORT).show() }
    }
}