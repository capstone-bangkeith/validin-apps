package com.safiraak.validin.presentation.view.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.safiraak.validin.R
import com.safiraak.validin.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.settingToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}