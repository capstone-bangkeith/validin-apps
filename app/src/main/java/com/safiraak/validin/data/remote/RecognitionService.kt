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
    @POST("/ktp/ocr2")
    suspend fun postRecognitionFile(
        @Part file: MultipartBody.Part,
//        @Part("location") location: RectF,
    ) : Response<RecognitionResponse>
}