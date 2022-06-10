package com.safiraak.validin.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.safiraak.validin.data.RecognitionData
import com.safiraak.validin.ml.DetectWithMetadata
import com.safiraak.validin.presentation.view.verify.RecognitionListener
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model

class ImageAnalyzer(context: Context, private val listener: RecognitionListener) :
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
        val tfImage = TensorImage.fromBitmap(toBitmap(imageProxy))
        val outputs = detectWithMetadata.process(tfImage)
            .detectionResultList.get(0)
        val resultLabel = outputs.categoryAsString
        val resultScore = outputs.scoreAsFloat
        val resultLocation = outputs.locationAsRectF
        listener(RecognitionData(resultLabel, resultScore,resultLocation))
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
        yuvToRgbConverter.yuvToRgb(image, bitmapBuffer)
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
    companion object {
        private const val TAG = "ImageAnalyzer"
    }
}