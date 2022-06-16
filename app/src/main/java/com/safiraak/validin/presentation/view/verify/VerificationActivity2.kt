package com.safiraak.validin.presentation.view.verify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.safiraak.validin.data.SetDataKtp
import com.safiraak.validin.databinding.ActivityVerification2Binding

class VerificationActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityVerification2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerification2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val datKtp = intent.getParcelableExtra<SetDataKtp>(DATA_EXTRA_KTP) as SetDataKtp
        binding.verifButNext.setOnClickListener {
            startActivity(Intent(this@VerificationActivity2, FinishActivity::class.java))
        }
    }

    fun setSomeText(setDataKtp: SetDataKtp) {
        binding.verif2NamaEdittext.setText(setDataKtp.nama)
        binding.verif2AlamatEdittext.setText(setDataKtp.alamat)
        binding.verif2RtrwEdittext.setText(setDataKtp.rtrw)
        binding.verif2KeldesEdittext.setText(setDataKtp.keldes)
        binding.verif2KecamEdittext.setText(setDataKtp.kecamatan)
        //agama
        if (setDataKtp.statPer?.lowercase() == "kawin") {
            binding.verif2RadioButtonKawin.isChecked
        } else {
            binding.verif2RadioButtonTdkkawin.isChecked
        }
        binding.verif2PekerjaanEdittext.setText(setDataKtp.pekerjaan)
        if (setDataKtp.kwn?.lowercase() == "wni") {
            binding.verif2RadioButtonWni.isChecked
        } else {
            binding.verif2RadioButtonWna.isChecked
        }
    }

    companion object{
        val DATA_EXTRA_KTP = "ktp_data"
    }
}