package com.safiraak.validin.presentation.view.verify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.safiraak.validin.R
import com.safiraak.validin.databinding.ActivityFinishBinding
import com.safiraak.validin.presentation.view.main.MainActivity

class FinishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFinishBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        Glide.with(this)
            .load(R.drawable.finish)
            .into(binding.logoGif)
        binding.buttonBackHome.setOnClickListener {
            startActivity(Intent(this@FinishActivity, MainActivity::class.java))
        }
    }
}