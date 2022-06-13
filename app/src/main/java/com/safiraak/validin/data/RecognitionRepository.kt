package com.safiraak.validin.data

import android.graphics.RectF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.safiraak.validin.data.remote.RecognitionRequest
import com.safiraak.validin.data.remote.RecognitionResponse
import com.safiraak.validin.data.remote.RecognitionService
import okhttp3.MultipartBody
import retrofit2.Retrofit
import javax.inject.Inject

class RecognitionRepository @Inject constructor(retrofit: Retrofit){
    private val _recognitionData = MutableLiveData<RecognitionData>()
    val recognitionData: LiveData<RecognitionData> = _recognitionData
    private val _uploadRequest = MutableLiveData<RecognitionRequest>()
    val recognitionRequest: LiveData<RecognitionRequest> = _uploadRequest

    private val retrofitRecogService = retrofit.create(RecognitionService::class.java)

    fun updateData(recognitions: RecognitionData) {
        _recognitionData.postValue(recognitions)
    }
    suspend fun photoUpload(photo: MultipartBody.Part) : Result<RecognitionResponse> {
        return try {
         val response = retrofitRecogService.postRecognitionFile(photo)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Error("Body is null")
                }
            } else {
                Result.Error("Response is not successful")
            }
        } catch (e: Exception) {
            Result.Error("Error Upload Data please try again : " + e.message)
        }
    }
}