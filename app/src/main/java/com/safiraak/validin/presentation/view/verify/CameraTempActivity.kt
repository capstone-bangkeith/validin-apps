package com.safiraak.validin.presentation.view.verify

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.safiraak.validin.R
import com.safiraak.validin.ml.DetectWithMetadata
import com.safiraak.validin.presentation.viewmodel.Recognition
import com.safiraak.validin.presentation.viewmodel.RecognitionViewModel
import com.safiraak.validin.utils.YuvToRgbConverter
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model
import java.util.concurrent.Executors

private const val MAX_RESULT_DISPLAY = 3 // Maximum number of results displayed
private const val TAG = "TFL Classify" // Name for logging
private const val REQUEST_CODE_PERMISSIONS = 999 // Return code after asking for permission
private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

typealias RecognitionListener = (recognition: List<Recognition>) -> Unit

class CameraTempActivity : AppCompatActivity() {

    // CameraX variables
    private lateinit var preview: Preview // Preview use case, fast, responsive view of the camera
    private lateinit var imageAnalyzer: ImageAnalysis // Analysis use case, for running ML code
    private lateinit var camera: Camera
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    private val resultRecyclerView by lazy { findViewById<RecyclerView>(R.id.recognitionResults) }

    private val viewFinder by lazy { findViewById<PreviewView>(R.id.viewFinder) }

    private val recogViewModel: RecognitionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_temp)

        if (allPermissionGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        val viewAdapter = RecognitionAdapter(this)
        resultRecyclerView.adapter = viewAdapter

        resultRecyclerView.itemAnimator = null

        recogViewModel.recognitionList.observe(this, { viewAdapter.submitList(it) })
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

            preview = Preview.Builder()
                .build()

            imageAnalyzer = ImageAnalysis.Builder()
                .setTargetResolution(Size(224, 224))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build().also { analysisUsecases: ImageAnalysis ->
                    analysisUsecases.setAnalyzer(cameraExecutor, ImageAnalyzer(this) { items ->
                        recogViewModel.updateData(items)
                    })
                }

            val cameraSelector =
                if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA))
                    CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                camera =
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
                preview.setSurfaceProvider(viewFinder.surfaceProvider)
            } catch (exc: Exception) {
                Log.e(TAG, "Uses case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private class ImageAnalyzer(context: Context, private val listener: RecognitionListener) :
        ImageAnalysis.Analyzer {
            private val detectWithMetadata: DetectWithMetadata by lazy {
                val compatList = CompatibilityList()
                val options = if (compatList.isDelegateSupportedOnThisDevice) {
                    Log.d(TAG, "This device is GPU Compatible")
                    Model.Options.Builder().setDevice(Model.Device.GPU).build()
                } else {
                    Log.d(TAG, "This device is GPU Incompatible ")
                    Model.Options.Builder().setNumThreads(4).build()
                }
                DetectWithMetadata.newInstance(context, options)
            }

        override fun analyze(imageProxy: ImageProxy) {
            val items = mutableListOf<Recognition>()
            val tfImage = TensorImage.fromBitmap(toBitmap(imageProxy))
            val outputs = detectWithMetadata.process(tfImage)
                .detectionResultList.get(0)
            val resultLabel = outputs.categoryAsString
            val resultScore = outputs.scoreAsFloat

            items.add(Recognition(resultLabel, resultScore))

            listener(items.toList())
            imageProxy.close()
        }

        private val yuvToRgbConverter = YuvToRgbConverter(context)
        private lateinit var bitmapBuffer: Bitmap
        private lateinit var rotationMatrix: Matrix

        @SuppressLint("UnsafeOptInUsageError")
        private fun toBitmap(imageProxy: ImageProxy): Bitmap? {
            val image = imageProxy.image ?: return null
            if (!::bitmapBuffer.isInitialized) {
                Log.d(TAG, "Initalise toBitmap()")
                rotationMatrix = Matrix()
                rotationMatrix.postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                bitmapBuffer = Bitmap.createBitmap(
                    imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888
                )
            }
            yuvToRgbConverter.yuv2Rgb(image, bitmapBuffer)
            return Bitmap.createBitmap(
                bitmapBuffer,
                0,
                0,
                bitmapBuffer.width,
                bitmapBuffer.height,
                rotationMatrix,
                false
            )
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}