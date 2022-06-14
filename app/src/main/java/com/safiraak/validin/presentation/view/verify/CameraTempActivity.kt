package com.safiraak.validin.presentation.view.verify

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.safiraak.validin.R
import com.safiraak.validin.data.RecognitionData
import com.safiraak.validin.data.Result
import com.safiraak.validin.data.SetDataKtp
import com.safiraak.validin.databinding.ActivityCameraTempBinding
import com.safiraak.validin.ml.DetectWithMetadata
import com.safiraak.validin.presentation.viewmodel.RecognitionViewModel
import com.safiraak.validin.utils.CamUtils
import com.safiraak.validin.utils.ImageAnalyzer
import com.safiraak.validin.utils.YuvToRgbConverter
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.math.min



typealias RecognitionListener = (recognition: RecognitionData) -> Unit

@AndroidEntryPoint
class CameraTempActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private lateinit var preview: Preview
    private lateinit var imageAnalyzer: ImageAnalysis
    private lateinit var camera: Camera
    private lateinit var binding: ActivityCameraTempBinding
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    private lateinit var photoMultiPart : MultipartBody.Part

    private val viewFinder by lazy { findViewById<PreviewView>(R.id.viewFinder) }

    private val recogViewModel: RecognitionViewModel by viewModels()
    private val captureDelay = 2000L
    private var alreadyTaken = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraTempBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        if (allPermissionGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        recogViewModel.recognitionData.observe(this) {
            binding.recognitionName.text = it.label
            binding.recognitionProb.text = it.probabilityString
            if (it.confidence > 0.99) {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (!alreadyTaken){
                        takePhoto(it.location)
                        alreadyTaken = true
                    }
                }, captureDelay)
            }
        }
        recogViewModel.recognitionResponse.observe(this) {
            when(it) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val nama = it.data?.data?.ktp?.nama
                    val nik = it.data?.data?.ktp?.nik
                    val alamat = it.data?.data?.ktp?.alamat
                    val tl = it.data?.data?.ktp?.tanggalLahir
                    val jk = it.data?.data?.ktp?.jenisKelamin
                    val prov = it.data?.data?.ktp?.provinsi
                    val kab = it.data?.data?.ktp?.kota
                    val kec = it.data?.data?.ktp?.kecamatan
                    val agama = it.data?.data?.ktp?.agama
                    val statPer = it.data?.data?.ktp?.statusPerkawinan
                    val pek = it.data?.data?.ktp?.pekerjaan
                    val kwn = it.data?.data?.ktp?.kewarganegaraan
                    val rtrw = it.data?.data?.ktp?.rtRw
                    val keldes = it.data?.data?.ktp?.kelDesa
                    val ktpUrl = it.data?.data?.user?.ktpUrl
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this@CameraTempActivity, VerificationActivity1::class.java)
                            .putExtra(VerificationActivity1.EXTRA_DATA_KTP, SetDataKtp(nik,prov, kab, kec, nama, jk, tl, agama, statPer, pek, kwn, ktpUrl,rtrw, keldes)))
                    }, captureDelay)
                }
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        alreadyTaken = false
        recogViewModel.recognitionData.observe(this) {
            binding.recognitionName.text = it.label
            binding.recognitionProb.text = it.probabilityString
            if (it.confidence > 0.99) {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (!alreadyTaken){
                        takePhoto(it.location)
                        alreadyTaken = true
                    }
                }, captureDelay)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.isShutdown
        imageAnalyzer.clearAnalyzer()
        finish()
    }
    private fun allPermissionGranted() : Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()) {
                startCamera()
            } else {
                showMessage("Permission denied")
                finish()
            }
        }
    }

    private fun  startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            preview = Preview.Builder().build()

            imageAnalyzer = ImageAnalysis.Builder()
                .setTargetResolution(Size(224, 224))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build().also { analysisUsecases: ImageAnalysis ->
                    analysisUsecases.setAnalyzer(cameraExecutor, ImageAnalyzer(this) { items ->
                        recogViewModel.updateData(items)
                    })
                }
            imageCapture = ImageCapture.Builder().build()

            val cameraSelector =
                if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA))
                    CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview,imageCapture, imageAnalyzer)
                preview.setSurfaceProvider(viewFinder.surfaceProvider)
            } catch (exc: Exception) {
                Log.e(TAG, "Uses case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }
    private fun takePhoto(location: RectF) {
        val imageCapture = imageCapture ?: return

        val file = CamUtils().makeFile(application)
        val requestPhoto = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

        photoMultiPart = MultipartBody.Part.createFormData("ktp", file.name, requestPhoto)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        val left = location.left
        val top = location.top
        val right = location.right
        val bottom = location.bottom
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, "$location", Toast.LENGTH_SHORT).show()
                    Log.d("LOCATION","$location")
                    recogViewModel.photoUpload(photoMultiPart, left, top, right, bottom)

                    Log.d(TAG, msg)
                }
            }
        )
    }
    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    companion object{
        private const val TAG = "CameraTempActivity"
        private const val REQUEST_CODE_PERMISSIONS = 999
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}