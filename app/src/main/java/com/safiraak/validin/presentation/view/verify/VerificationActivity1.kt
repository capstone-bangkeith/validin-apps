package com.safiraak.validin.presentation.view.verify

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.safiraak.validin.R
import com.safiraak.validin.data.Result
import com.safiraak.validin.databinding.ActivityVerification1Binding
import com.safiraak.validin.presentation.viewmodel.RecognitionViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class VerificationActivity1 : AppCompatActivity() {
    private lateinit var binding: ActivityVerification1Binding
    private val recogViewModel: RecognitionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerification1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        recogViewModel.setDataKtpResponse.observe(this) {
            if(it != null) {
                binding.verifNiktxtObj.text = it.nik
                binding.verifJeniskeltxtObj.text = it.jk
                binding.verifTgltxtObj.text = it.ttl
                binding.verifKabtxtObj.text = it.kabupaten
                binding.verifProvtxtObj.text = it.provinsi
                Glide.with(this)
                    .load(it.url)
                    .error(R.drawable.ktp_detected)
                    .into(binding.verifKtpimage)
            }else{
                binding.verifNiktxtObj.text = "-"
                binding.verifJeniskeltxtObj.text = "-"
                binding.verifTgltxtObj.text = "-"
                binding.verifKabtxtObj.text = "-"
                binding.verifProvtxtObj.text = "-"
                binding.verifKtpimage.setImageResource(R.drawable.ktp_detected)
            }
        }
        binding.verifButNext.setOnClickListener { startActivity(Intent(this, VerificationActivity2::class.java)) }
        binding.verifButCapagain.setOnClickListener {
            startActivity(Intent(this, CameraTempActivity::class.java))
            finish()
        }
        }

    companion object {
        private val TAG = "VerificationActivity1"
        val EXTRA_DATA_KTP = "data_ktp"
    }
}