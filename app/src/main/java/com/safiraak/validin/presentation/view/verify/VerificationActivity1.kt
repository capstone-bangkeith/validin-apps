package com.safiraak.validin.presentation.view.verify

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.safiraak.validin.R
import com.safiraak.validin.data.Result
import com.safiraak.validin.data.SetDataKtp
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
        val ktp = intent.getParcelableExtra<SetDataKtp>(EXTRA_DATA_KTP) as SetDataKtp
        dataKtp(setDataKtp = ktp)
        binding.verifButNext.setOnClickListener { startActivity(Intent(this, VerificationActivity2::class.java)) }
        binding.verifButCapagain.setOnClickListener {
            startActivity(Intent(this, CameraTempActivity::class.java)
                .putExtra(VerificationActivity2.DATA_EXTRA_KTP, ktp))
            finish()
        }
    }
    private fun dataKtp(setDataKtp: SetDataKtp) {
        binding.verifNiktxt.text = setDataKtp.nik
        binding.verifJeniskeltxt.text = setDataKtp.jk
        binding.verifTgltxt.text = setDataKtp.ttl
        binding.verifKecamattxt.text = setDataKtp.kecamatan
        binding.verifKabtxt.text = setDataKtp.kabupaten
        binding.verifProvtxt.text = setDataKtp.provinsi
        Glide.with(this)
            .load(setDataKtp.url)
            .error(R.drawable.ktp_detected)
            .into(binding.verifKtpimage)
    }

    companion object {
        private val TAG = "VerificationActivity1"
        val EXTRA_DATA_KTP = "data_ktp"
    }
}