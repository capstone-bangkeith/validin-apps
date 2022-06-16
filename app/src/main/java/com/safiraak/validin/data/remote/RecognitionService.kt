package com.safiraak.validin.data.remote

import android.graphics.RectF
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface RecognitionService {
    @Multipart
    @POST("/ktp/ocr2")
    suspend fun postRecognitionFile(
        @Part ktp: MultipartBody.Part,
        @Part("left") left: Float,
        @Part("top") top: Float,
        @Part("right") right: Float,
        @Part("bottom") bottom: Float
    ) : Response<RecognitionResponse>

    @Multipart
    @POST("/ktp/")
    suspend fun postFinalForm(
        @Part("provinsi") provinsi: RequestBody,
        @Part("kota") kota: RequestBody,
        @Part("nik") nik: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("ttl") ttl: RequestBody,
        @Part("jenis_kelamin") jenis_kelamin: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("rt_rw") rt_rw: RequestBody,
        @Part("kel_desa") kel_desa: RequestBody,
        @Part("kecamatan") kecamatan: RequestBody,
        @Part("agama") agama: RequestBody,
        @Part("status_perkawinan") status_perkawinan: RequestBody,
        @Part("pekerjaan") pekerjaan: RequestBody,
        @Part("kewarganegaraan") kewarganegaraan: RequestBody,
    ) : Response<FinalFormResponse>

    @GET("/ktp/")
    suspend fun getAllData() : Response<CheckDataResponse>
}