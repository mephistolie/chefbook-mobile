//package com.cactusknights.chefbook.legacy.activities
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.text.SpannableString
//import android.text.Spanned
//import android.text.style.ForegroundColorSpan
//import android.view.View
//import androidx.activity.result.ActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.lifecycleScope
//import com.cactusknights.chefbook.R
//import com.cactusknights.chefbook.databinding.ActivityLoginBinding
//import com.cactusknights.chefbook.legacy.enums.SignStates
//import com.cactusknights.chefbook.legacy.helpers.Utils
//import com.cactusknights.chefbook.legacy.helpers.Utils.hideKeyboard
//import com.cactusknights.chefbook.legacy.helpers.showToast
//import com.cactusknights.chefbook.models.User
//import com.cactusknights.chefbook.legacy.viewmodels.UuuserViewModel
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.api.ApiException
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.flow.collect
//import kotlinx.coroutines.launch
//
//@AndroidEntryPoint
//open class LoginActivity : AppCompatActivity() {
//
//    private var state = SignStates.SIGN_IN
//    private val userViewModel: UuuserViewModel by viewModels()
//
//    private lateinit var binding: ActivityLoginBinding
//
//    private val googleLogInContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
//        if (result?.resultCode == Activity.RESULT_OK) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            try {
//                val account = task.getResult(ApiException::class.java)!!
//                userViewModel.logonGoogle(account.idToken!!, ::onLogInCallback)
//            } catch (e: ApiException) {}
//        } else {
//            applicationContext.showToast(resources.getString(R.string.failed_to_google))
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val chefbookTitle = SpannableString("ChefBook")
//        chefbookTitle.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.deep_orange)), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        chefbookTitle.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.monochrome_invert)), 4, chefbookTitle.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        binding.textAppName.text = chefbookTitle
//
//        userViewModel.logout()
//
//        binding.btnLogin.setOnClickListener {
//            when(state) {
//                SignStates.SIGN_IN -> { login() }
//                SignStates.SIGN_UP -> { signUp() }
//                else -> { restorePassword() }
//            }
//        }
//
//        binding.btnGoogle.setOnClickListener {
//            logonGoogle()
//        }
//
//        binding.textSignUp.setOnClickListener {
//            if (state == SignStates.SIGN_IN) {
//                state = SignStates.SIGN_UP
//                setSignUpLayout()
//            } else {
//                state = SignStates.SIGN_IN
//                setLoginLayout()
//            }
//        }
//
//        binding.btnResetPassword.setOnClickListener {
//            setRestoreLayout()
//            state = SignStates.RESTORE_PASSWORD
//        }
//    }
//
//    override fun onStart() {
//        lifecycleScope.launch { listenToUser() }
//        super.onStart()
//    }
//
//    private suspend fun listenToUser() {
//        userViewModel.listenToUser().collect { user: User? ->
//            if (user != null) {
//                applicationContext.showToast(R.string.login_successfully)
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//            }
//        }
//    }
//
//    private fun setSignUpLayout() {
//
//        binding.inputPassword.setText("")
//        binding.inputRepeatPassword.setText("")
//
//        binding.ilPassword.visibility = View.VISIBLE
//        binding.ilRepeatPassword.visibility = View.VISIBLE
//        binding.viewSeparator.visibility = View.GONE
//        binding.llProviders.visibility = View.GONE
//
//        binding.btnLogin.text = resources.getText(R.string.signup)
//        binding.textMembership.text = resources.getText(R.string.already_member)
//        binding.textSignUp.text = resources.getText(R.string.login)
//    }
//
//    private fun setLoginLayout() {
//
//        binding.inputPassword.text = null
//
//        binding.ilPassword.visibility = View.VISIBLE
//        binding.ilRepeatPassword.visibility = View.GONE
//        binding.viewSeparator.visibility = View.VISIBLE
//        binding.llProviders.visibility = View.VISIBLE
//
//        binding.textMembership.text = resources.getText(R.string.not_member)
//        binding.textSignUp.text = resources.getText(R.string.signup)
//        binding.btnLogin.text = resources.getText(R.string.login)
//    }
//
//    private fun setRestoreLayout() {
//
//        binding.ilPassword.visibility = View.GONE
//        binding.ilRepeatPassword.visibility = View.GONE
//        binding.viewSeparator.visibility = View.GONE
//        binding.llProviders.visibility = View.GONE
//
//        binding.btnLogin.text = resources.getText(R.string.reset_password)
//        binding.textMembership.text = resources.getText(R.string.already_member)
//        binding.textSignUp.text = resources.getText(R.string.login)
//
//        state = SignStates.RESTORE_PASSWORD
//    }
//
//    private fun changeLoginProgressLayout() {
//        binding.btnLogin.visibility = if (binding.btnLogin.visibility == View.VISIBLE) View.GONE else View.VISIBLE
//        binding.progressLogin.visibility = if (binding.progressLogin.visibility == View.VISIBLE) View.GONE else View.VISIBLE
//    }
//
//    private fun login() {
//        val emailText = binding.inputEmail.text.toString()
//        val passwordText = binding.inputPassword.text.toString()
//        if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
//            userViewModel.logonEmail(binding.inputEmail.text.toString(), binding.inputPassword.text.toString(), ::onLogInCallback)
//            changeLoginProgressLayout()
//        } else {
//            applicationContext.showToast(R.string.empty_fields)
//        }
//    }
//
//    private fun signUp() {
//        val emailText = binding.inputEmail.text.toString()
//        val passwordText = binding.inputPassword.text.toString()
//        val repeatPasswordText = binding.inputRepeatPassword.text.toString()
//        if (Utils.checkAuthFields(emailText, passwordText, repeatPasswordText, this)) {
//            userViewModel.signup(emailText, passwordText, ::onSignUpCallback)
//            changeLoginProgressLayout()
//        }
//    }
//
//    private fun restorePassword() {
//        val emailText = binding.inputEmail.text.toString()
//        if (!emailText.contains('@') || !emailText.contains('.')) {
//            applicationContext.showToast(R.string.invalid_email)
//        } else {
//            userViewModel.restorePassword(emailText, ::onRestorePasswordCallback)
//            changeLoginProgressLayout()
//            hideKeyboard(this)
//        }
//    }
//
//    private fun logonGoogle() {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        val googleSignInClient = GoogleSignIn.getClient(this, gso)
//        val signInIntent = googleSignInClient.signInIntent
//        googleLogInContract.launch(signInIntent)
//    }
//
//    private fun onLogInCallback(isDeleted: Boolean) {
//        if (isDeleted) { applicationContext.showToast(R.string.login_successfully) }
//        else {
//            applicationContext.showToast(R.string.authentication_failed)
//            changeLoginProgressLayout()
//        }
//    }
//
//    private fun onSignUpCallback(isDeleted: Boolean) {
//        if (isDeleted) { applicationContext.showToast(R.string.signup_successfully) }
//        else {
//            applicationContext.showToast(R.string.signup_failed)
//            changeLoginProgressLayout()
//        }
//
//    }
//
//    private fun onRestorePasswordCallback(isDeleted: Boolean) {
//        if (isDeleted) { applicationContext.showToast(R.string.reset_password_successfully) }
//        else {
//            applicationContext.showToast(R.string.reset_password_failed)
//            changeLoginProgressLayout()
//        }
//    }
//}