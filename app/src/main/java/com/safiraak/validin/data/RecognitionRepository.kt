package com.safiraak.validin.data

import android.app.Application
import android.content.ContentValues
import android.graphics.RectF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.safiraak.validin.data.remote.UploadRequest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class RecognitionRepository @Inject constructor(application: Application){
    private val _recognitionData = MutableLiveData<RecognitionData>()
    val recognitionData: LiveData<RecognitionData> = _recognitionData
    private val _uploadRequest = MutableLiveData<UploadRequest>()
    val uploadRequest: LiveData<UploadRequest> = _uploadRequest

    fun updateData(recognitions: RecognitionData) {
        _recognitionData.postValue(recognitions)
    }
    fun PhotoUpload(photo: File, location: RectF) {
        _uploadRequest.postValue(UploadRequest(photo, location))
    }
}