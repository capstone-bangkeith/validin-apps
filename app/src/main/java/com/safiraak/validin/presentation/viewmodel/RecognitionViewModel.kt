package com.safiraak.validin.presentation.viewmodel

import android.graphics.RectF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.safiraak.validin.data.AuthInterceptor
import com.safiraak.validin.data.RecognitionData
import com.safiraak.validin.data.RecognitionRepository
import com.safiraak.validin.data.Result
import com.safiraak.validin.data.remote.RecognitionRequest
import com.safiraak.validin.data.remote.RecognitionResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class RecognitionViewModel @Inject constructor(
    private val repository: RecognitionRepository
) : ViewModel() {
    private val _recognitionData = repository.recognitionData
    val recognitionData: LiveData<RecognitionData> = _recognitionData
    private val _recognitionRequest = repository.recognitionRequest
    val recognitionRequest: LiveData<RecognitionRequest> = _recognitionRequest
    private val _recognitionResponse = MutableLiveData<Result<RecognitionResponse>>()
    val recognitionResponse: LiveData<Result<RecognitionResponse>> = _recognitionResponse

    fun updateData(recognitions: RecognitionData) {
        viewModelScope.launch {
            return@launch repository.updateData(recognitions)
        }
    }

    fun photoUpload(photo: MultipartBody.Part) {
        _recognitionResponse.postValue(Result.Loading())
        viewModelScope.launch {
            _recognitionResponse.postValue(repository.photoUpload(photo))
        }
    }
}


