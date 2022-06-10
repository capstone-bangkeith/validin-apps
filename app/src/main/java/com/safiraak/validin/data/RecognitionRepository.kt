package com.safiraak.validin.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class RecognitionRepository @Inject constructor(application: Application){
    private val _recognitionData = MutableLiveData<RecognitionData>()
    val recognitionData: LiveData<RecognitionData> = _recognitionData

    fun updateData(recognitions: RecognitionData) {
        _recognitionData.postValue(recognitions)
    }
}