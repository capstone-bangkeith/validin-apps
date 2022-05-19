package com.safiraak.validin.presentation.view

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.safiraak.validin.databinding.ActivityVerification1Binding
import com.safiraak.validin.utils.CamUtils
import java.io.File

class VerificationActivity1 : AppCompatActivity() {
    private lateinit var binding: ActivityVerification1Binding
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerification1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val file = intent.extras?.get("picture") as File?
        val backCam = intent.extras?.get("backCam") as Boolean

        val result = CamUtils().rotateBitmap(
            BitmapFactory.decodeFile(file?.path),
            backCam
        )
        val backFile = CamUtils().bitmap2File(this, result)
        getFile = backFile

        binding.verifKtpimage.setImageBitmap(result)

        binding.verifButNext.setOnClickListener { startActivity(Intent(this, VerificationActivity2::class.java)) }
        binding.verifButCapagain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    companion object {
        const val CAMERAX_RESULT = 200
    }
}