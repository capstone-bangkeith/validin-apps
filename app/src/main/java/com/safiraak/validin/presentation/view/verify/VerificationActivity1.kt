package com.safiraak.validin.presentation.view.verify

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.safiraak.validin.databinding.ActivityVerification1Binding
import com.safiraak.validin.presentation.viewmodel.RecognitionViewModel
import com.safiraak.validin.utils.CamUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class VerificationActivity1 : AppCompatActivity() {
    private lateinit var binding: ActivityVerification1Binding
    private var getFile: File? = null
    private var ABheight: Int = 0
    private val recogViewModel: RecognitionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerification1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

//        val file = intent.extras?.get("picture") as File?
//        val backCam = intent.extras?.get("backCam") as Boolean
//        recogViewModel.recognitionRequest.observe(this) {
//            if (it != null) {
//                var result = CamUtils().rotateBitmap(
//                    BitmapFactory.decodeFile(it.photo.path),
//                )
//                //ABheight = resources.getDimension(android.R.attr.actionBarSize).toInt()
//                result = Bitmap.createBitmap(result, 16, (result.height/2)-125-56, result.width-16, (result.height/2)+125-56)
//                val backFile = CamUtils().bitmap2File(this, result)
//                getFile = backFile
//                binding.verifKtpimage.setImageBitmap(result)
//                binding.verifNiktxt.text = it.location.toString()
//            } else {
//                Toast.makeText(baseContext, "NULL", Toast.LENGTH_SHORT).show()
//            }
//        }


        binding.verifButNext.setOnClickListener { startActivity(Intent(this, VerificationActivity2::class.java)) }
        binding.verifButCapagain.setOnClickListener {
            startActivity(Intent(this, CameraxActivity::class.java))
            finish()
        }
    }

    companion object {
        const val CAMERAX_RESULT = 200
    }
}