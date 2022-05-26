package com.safiraak.validin.presentation.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.safiraak.validin.R
import com.safiraak.validin.databinding.ActivityCameraxBinding
import com.safiraak.validin.utils.CamUtils
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.time.Duration

class CameraxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraxBinding

    private lateinit var camExecute: ExecutorService

    private var camSelect: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private var imageCapt: ImageCapture? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_CODE_PERMIS) {
            if (!permissionGranted()) {
                showMessage("Permission not granted")
                finish()
            }
        }
    }

    private fun permissionGranted() = REQ_PERMISSION.all { ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.camxToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (!permissionGranted()) {
            ActivityCompat.requestPermissions(this, REQ_PERMISSION, REQ_CODE_PERMIS)
        }

        camExecute = Executors.newSingleThreadExecutor()

        toastInstruction()

        binding.camCapture.setOnClickListener {
            takePict()
        }
    }

    override fun onResume() {
        super.onResume()
        toastInstruction()
        startCam()
    }

    override fun onDestroy() {
        super.onDestroy()
        camExecute.shutdown()
    }

    private fun startCam() {
        val camProviderFuture = ProcessCameraProvider.getInstance(this)
        camProviderFuture.addListener({
            val camProvide: ProcessCameraProvider = camProviderFuture.get()
            val preview = Preview.Builder().build().also { it.setSurfaceProvider(binding.camPreview.surfaceProvider) }
            imageCapt = ImageCapture.Builder().build()
            try {
                camProvide.unbindAll()
                camProvide.bindToLifecycle(this, camSelect, preview, imageCapt)
            } catch (e : Exception) { showMessage("Cant take") }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePict() {
        val imageCapture = imageCapt ?: return
        val file = CamUtils().makeFile(application)
        val outOption = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture.takePicture(outOption, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val intent = Intent(this@CameraxActivity, VerificationActivity1::class.java)
                intent.putExtra("picture", file)
                intent.putExtra("backCam", camSelect == CameraSelector.DEFAULT_BACK_CAMERA)
                setResult(VerificationActivity1.CAMERAX_RESULT, intent)
                startActivity(intent)
            }

            override fun onError(exception: ImageCaptureException) {
                showMessage("cant take")
            }

        })
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun toastDecline() {
        val layout : View = layoutInflater.inflate(R.layout.toast_decline, findViewById(R.id.ll_toastDec))
        Toast(this).apply {
            duration = Toast.LENGTH_LONG
            setGravity(Gravity.CENTER, 0, 0)
            view = layout
        }.show()
    }

    private fun toastInstruction() {
        val layout : View = layoutInflater.inflate(R.layout.toast_instruction, findViewById(R.id.ll_toastIns))
        Toast(this).apply {
            duration = Toast.LENGTH_LONG
            setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            view = layout
        }.show()
    }

    companion object {
        private val REQ_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQ_CODE_PERMIS = 10
    }
}