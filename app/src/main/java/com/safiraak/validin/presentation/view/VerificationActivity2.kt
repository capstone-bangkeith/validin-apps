package com.safiraak.validin.presentation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.safiraak.validin.databinding.ActivityVerification2Binding

class VerificationActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityVerification2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerification2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}