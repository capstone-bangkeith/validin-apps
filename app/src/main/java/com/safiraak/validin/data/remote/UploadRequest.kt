package com.safiraak.validin.data.remote

import android.graphics.RectF
import java.io.File

data class UploadRequest(
    val photo: File,
    val location: RectF
    )