package com.safiraak.validin.utils

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Matrix
import com.safiraak.validin.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class CamUtils {
    val FILENAME_FORMAT = "yyyy-MM-dd-HH-ss"

    val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    fun makeFile(application: Application): File {
        val dir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        val outDir = if (dir != null && dir.exists()) dir else application.filesDir

        return File(outDir, "$timeStamp.jpg")
    }

    fun bitmap2File(context: Context, bitmap: Bitmap) : File {
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        val streamOut: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, streamOut)
        streamOut.flush()
        streamOut.close()
        return file
    }

    fun rotateBitmap(bitmap: Bitmap, backCam: Boolean = false): Bitmap {
        val matrix = Matrix()
        return if (backCam) {
            matrix.postRotate(90f)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            matrix.postRotate(-90f)
            matrix.postScale(-1f, 1f, bitmap.width/2f, bitmap.height/2f)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }
}