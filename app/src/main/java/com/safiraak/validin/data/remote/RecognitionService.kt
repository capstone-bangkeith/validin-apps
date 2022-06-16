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
        @Part("provinsi") provinsi: String,
        @Part("kota") kota: String,
        @Part("nik") nik: String,
        @Part("nama") nama: String,
        @Part("ttl") ttl: String,
        @Part("jenis_kelamin") jenis_kelamin: String,
        @Part("alamat") alamat: String,
        @Part("rt_rw") rt_rw: String,
        @Part("kel_desa") kel_desa: String,
        @Part("kecamatan") kecamatan: String,
        @Part("agama") agama: String,
        @Part("status_perkawinan") status_perkawinan: String,
        @Part("pekerjaan") pekerjaan: String,
        @Part("kewarganegaraan") kewarganegaraan: String,
    ) : Response<FinalFormResponse>

//    @GET("/ktp/")
//    suspend fun getAllData(
//
//    ) : Response
}