package com.safiraak.validin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecognitionViewModel : ViewModel() {
    private val theRecognitionList = MutableLiveData<List<Recognition>>()
    val recognitionList: LiveData<List<Recognition>> = theRecognitionList

    fun updateData(recognitions: List<Recognition>) {
        theRecognitionList.postValue(recognitions)
    }
}

data class Recognition(val label: String, val confidence: Float) {
    override fun toString(): String {
        return "$label / $probabilityString"
    }
    val probabilityString = String.format("%.1f%%", confidence*100.0f)
}