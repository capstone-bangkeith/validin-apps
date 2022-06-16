package com.safiraak.validin.presentation.view.verify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.get
import com.safiraak.validin.data.SetDataKtp

import com.safiraak.validin.databinding.ActivityVerification2Binding
import com.safiraak.validin.presentation.viewmodel.RecognitionViewModel


import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class VerificationActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityVerification2Binding
    private val recogViewModel: RecognitionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerification2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val datKtp = intent.getParcelableExtra<SetDataKtp>(DATA_EXTRA_KTP) as? SetDataKtp
        if (datKtp != null) {
            setSomeText(datKtp)
        }

        binding.verifButNext.setOnClickListener {
            val prov = binding.verif2ProvEdittext.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val kota = binding.verif2KabEdittext.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val nik = datKtp?.nik.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val nama = binding.verif2NamaEdittext.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val ttl = binding.verif2TtlEdittext.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val jk = binding.verif2JkEdittext.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val alamat = binding.verif2AlamatEdittext.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val rtrw = binding.verif2RtrwEdittext.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val keldes = binding.verif2KeldesEdittext.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val kec = binding.verif2KecamEdittext.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val agama = binding.verif2AgamaEdittext.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val statPerId = binding.verif2RadioGSp.checkedRadioButtonId
            val statPer = findViewById<RadioButton>(statPerId).text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val pekerjaan = binding.verif2PekerjaanEdittext.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val kwnId = binding.verif2RadioGKwn.checkedRadioButtonId
            val kwn = findViewById<RadioButton>(kwnId).text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            recogViewModel.finalformUp(prov, kota, nik, nama, ttl, jk, alamat, rtrw, keldes, kec, agama, statPer, pekerjaan, kwn)
            Log.d("provEdit", prov.toString())
            startActivity(Intent(this@VerificationActivity2, FinishActivity::class.java))
        }
    }

    fun setSomeText(setDataKtp: SetDataKtp) {
        binding.verif2NamaEdittext.setText(setDataKtp.nama)
        binding.verif2AlamatEdittext.setText(setDataKtp.alamat)
        binding.verif2TtlEdittext.setText(setDataKtp.ttl)
        binding.verif2JkEdittext.setText(setDataKtp.jk)
        binding.verif2RtrwEdittext.setText(setDataKtp.rtrw)
        binding.verif2KeldesEdittext.setText(setDataKtp.keldes)
        binding.verif2KecamEdittext.setText(setDataKtp.kecamatan)
        binding.verif2KabEdittext.setText(setDataKtp.kabupaten)
        binding.verif2ProvEdittext.setText(setDataKtp.provinsi)
        binding.verif2AgamaEdittext.setText(setDataKtp.agama)
        if (setDataKtp.statPer?.lowercase().toString() == "kawin") {
            binding.verif2RadioButtonKawin.isChecked = true
        } else {
            binding.verif2RadioButtonTdkkawin.isChecked = true
        }
        binding.verif2PekerjaanEdittext.setText(setDataKtp.pekerjaan)
        if (setDataKtp.kwn?.lowercase().toString() == "wni") {
            binding.verif2RadioButtonWni.isChecked = true
        } else {
            binding.verif2RadioButtonWna.isChecked = true
        }
    }

    companion object{
        val DATA_EXTRA_KTP = "ktp_data"
    }
}