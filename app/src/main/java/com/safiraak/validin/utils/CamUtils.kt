package com.safiraak.validin.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.view.View
import com.safiraak.validin.R
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class CamUtils {
    val FILENAME_FORMAT = "yyyy-MM-dd-HH-ss"

    val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    fun cropImage(bitmap: Bitmap, frame: View, reference: View): ByteArray {
        val heightOriginal = frame.height
        val widthOriginal = frame.width
        val heightFrame = reference.height
        val widthFrame = reference.width
        val leftFrame = reference.left
        val topFrame = reference.top
        val heightReal = bitmap.height
        val widthReal = bitmap.width
        val widthFinal = widthFrame * widthReal / widthOriginal
        val heightFinal = heightFrame * heightReal / heightOriginal
        val leftFinal = leftFrame * widthReal / widthOriginal
        val topFinal = topFrame * heightReal / heightOriginal
        val bitmapFinal = Bitmap.createBitmap(
            bitmap,
            leftFinal, topFinal, widthFinal, heightFinal
        )
        val stream = ByteArrayOutputStream()
        bitmapFinal.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            stream
        )
        return stream.toByteArray()
    }

    fun makeFile(application: Application): File {
        val dir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        val outDir = if (dir != null && dir.exists()) dir else application.filesDir

        return File(outDir, "$timeStamp.jpg")
    }

    fun reduceSize(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var theQuality = 100
        var lengthStream: Int

        do {
            val streamBitmap = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,theQuality, streamBitmap)
            val pictByteArray = streamBitmap.toByteArray()
            lengthStream = pictByteArray.size
            theQuality -= 5
        } while (lengthStream > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, theQuality, FileOutputStream(file))
        return file
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

    fun uri2File(selectImage: Uri, context: Context): File {
        val contentResolve: ContentResolver = context.contentResolver
        val file = createTempFile(context)
        val inputStream = contentResolve.openInputStream(selectImage) as InputStream
        val outputStream: OutputStream = FileOutputStream(file)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()
        return file
    }

    fun createTempFile(context: Context): File {
        val dir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", dir)
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