package com.safiraak.validin.presentation.view.auth

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.safiraak.validin.R
import com.safiraak.validin.databinding.ActivityLoginBinding
import com.safiraak.validin.presentation.view.main.MainActivity
import com.safiraak.validin.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val userViewModel: UserViewModel by viewModels()
    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG,"FirebaseAuth : " + account.id)
                account.idToken?.let {
                    userViewModel.userLoginGoogle(it)
                }
            } catch (e: Exception) {
                Log.w(TAG, "Sign in failed", e)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val registerText = SpannableString(getString(R.string.tv_regist))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(tv: View) {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE
                ds.isUnderlineText = false
            }
        }

        userViewModel.user.observe(this) {
            if (it != null) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }

        userViewModel.isLogout.observe(this) {
            if (!it) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }

        val language = Locale.getDefault().language

        if(language == "en") {
            registerText.setSpan(clickableSpan,28,36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else if (language == "in") {
            registerText.setSpan(clickableSpan,18,26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvRegister.apply {
            text = registerText
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }

        //Google SignIn
        val googleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        //FirebaseAuth
        auth = Firebase.auth

        binding.btnGoogleLogin.setOnClickListener {
            signInGoogle()
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailField.text.toString().trim()
            val password = binding.etPasswordField.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                binding.etEmailField.error = "Email is required"
                binding.etPasswordField.error = "Password is required"
            } else {
                //firebaseAuthWithEmailPassword(email, password)
                userViewModel.userLogin(email, password)
            }
        }

        binding.tvForgetpassword.setOnClickListener {
            PopUpForgetPass().show(supportFragmentManager, "PopUpFP")
        }
    }

    fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}