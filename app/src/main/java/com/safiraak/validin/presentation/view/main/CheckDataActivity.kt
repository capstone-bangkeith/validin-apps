package com.safiraak.validin.presentation.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.safiraak.validin.R
import com.safiraak.validin.data.Result
import com.safiraak.validin.databinding.ActivityCheckDataBinding
import com.safiraak.validin.presentation.viewmodel.RecognitionViewModel

class CheckDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckDataBinding
    private val recogViewModel: RecognitionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.checkToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recogViewModel.checkData()
        recogViewModel.checkdataResponse.observe(this) {
            when(it) {
                is Result.Success -> {

                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}