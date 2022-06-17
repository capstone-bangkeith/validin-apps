package com.safiraak.validin.presentation.view.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.safiraak.validin.databinding.ActivitySettingBinding
import com.safiraak.validin.presentation.viewmodel.ThemeViewModel
import com.safiraak.validin.presentation.viewmodel.ThemeViewModelFactory
import com.safiraak.validin.utils.ThemePreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "themes")

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = ThemePreference.getInstance(dataStore)
        val themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref)).get(
            ThemeViewModel::class.java
        )

        setSupportActionBar(binding.settingToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.buttonLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        themeViewModel.getThemeSet().observe(this, { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        })

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isSwitch: Boolean ->
            themeViewModel.saveThemeSet(isSwitch)
        }
    }
}