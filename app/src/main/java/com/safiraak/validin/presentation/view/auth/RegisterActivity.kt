package com.safiraak.validin.presentation.view.auth

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.safiraak.validin.R
import com.safiraak.validin.databinding.ActivityRegisterBinding
import com.safiraak.validin.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.safiraak.validin.presentation.view.main.MainActivity
import java.util.*

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        userViewModel.user.observe(this) {
            if (it != null) {
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            }
        }
        val loginText = SpannableString(getString(R.string.tv_signin))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(tv: View) {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE
                ds.isUnderlineText = false
            }
        }

        val language = Locale.getDefault().language

        if (language == "en") {
            loginText.setSpan(clickableSpan,25,32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else if (language == "in") {
            loginText.setSpan(clickableSpan,18,25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvRegister.apply {
            text = loginText
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }

        //FirebaseAuth
        auth = Firebase.auth


        binding.btnRegist.setOnClickListener {
            val email = binding.etEmailField.text.toString().trim()
            val password = binding.etConfirmpasswordField.text.toString().trim()
            val passwordOne = binding.etPasswordField.toString().trim()
            if (binding.etPasswordField.text.toString() != binding.etConfirmpasswordField.text.toString()) {
                binding.etConfirmpasswordField.error = getString(R.string.mismatch_pass)
                return@setOnClickListener
            }
            if (email.isEmpty() || password.isEmpty() || passwordOne.isEmpty()) {
                binding.etEmailField.error = "Email is Required"
                binding.etPasswordField.error = "Password is Required"
                binding.etConfirmpasswordField.error = "Confirm Password is Required"
            } else {
                userViewModel.userRegister(email, password)
            }
        }
    }
}