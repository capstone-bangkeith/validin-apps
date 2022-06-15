package com.safiraak.validin.data.remote

import android.graphics.RectF
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RecognitionService {
    @Multipart
    @POST("/ktp/ocr2?rotate=1")
    suspend fun postRecognitionFile(
        @Part ktp: MultipartBody.Part,
        @Part("left") left: Float,
        @Part("top") top: Float,
        @Part("right") right: Float,
        @Part("bottom") bottom: Float
    ) : Response<RecognitionResponse>
}