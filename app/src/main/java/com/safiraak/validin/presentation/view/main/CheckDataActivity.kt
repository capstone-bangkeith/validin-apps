package com.safiraak.validin.presentation.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.safiraak.validin.R
import com.safiraak.validin.databinding.ActivityCheckDataBinding

class CheckDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.checkToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}