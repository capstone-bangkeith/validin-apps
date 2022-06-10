package com.safiraak.validin.presentation.viewmodel

import android.graphics.RectF
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safiraak.validin.data.RecognitionData
import com.safiraak.validin.data.RecognitionRepository
import com.safiraak.validin.data.remote.UploadRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RecognitionViewModel @Inject constructor(
    private val repository: RecognitionRepository
) : ViewModel() {
    private val _recognitionData = repository.recognitionData
    val recognitionData: LiveData<RecognitionData> = _recognitionData
    private val _uploadRequest = repository.uploadRequest
    val uploadRequest: LiveData<UploadRequest> = _uploadRequest

    fun updateData(recognitions: RecognitionData) {
        viewModelScope.launch {
            return@launch repository.updateData(recognitions)
        }
    }

    fun photoUpload(photo: File, location: RectF) {
        viewModelScope.launch {
            return@launch repository.PhotoUpload(photo, location)
        }
    }
}

