package com.safiraak.validin.presentation.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.safiraak.validin.R
import com.safiraak.validin.data.Result
import com.safiraak.validin.databinding.ActivityCheckDataBinding
import com.safiraak.validin.presentation.viewmodel.RecognitionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
                    binding.tvFillNik.text = it.data?.data?.nik.toString()
                    binding.tvFillNama.text = it.data?.data?.nama.toString()
                    binding.tvFillKab.text = it.data?.data?.kota.toString()
                    binding.tvFillProv.text = it.data?.data?.provinsi.toString()
                    binding.tvFillRtrw.text = it.data?.data?.rtRw.toString()
                    binding.tvFillJk.text = it.data?.data?.jenisKelamin.toString()
                    binding.tvFillKeldes.text = it.data?.data?.kelDesa.toString()
                    binding.tvFillKec.text = it.data?.data?.kecamatan.toString()
                    binding.tvFillAgama.text = it.data?.data?.agama.toString()
                    binding.tvFillSp.text = it.data?.data?.statusPerkawinan.toString()
                    binding.tvFillPkrj.text = it.data?.data?.pekerjaan.toString()
                    binding.tvFillKwn.text = it.data?.data?.kewarganegaraan.toString()
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