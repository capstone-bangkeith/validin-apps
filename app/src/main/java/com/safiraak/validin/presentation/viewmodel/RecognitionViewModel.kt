package com.safiraak.validin.presentation.viewmodel

import android.graphics.RectF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.safiraak.validin.data.*
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
    private val _recognitionResponse = MutableLiveData<Result<RecognitionResponse>>()
    val recognitionResponse: LiveData<Result<RecognitionResponse>> = _recognitionResponse

    private val _setDataKtpResponse = MutableLiveData<SetDataKtp>()
    val setDataKtpResponse: LiveData<SetDataKtp> = _setDataKtpResponse


    fun updateData(recognitions: RecognitionData) {
        viewModelScope.launch {
            return@launch repository.updateData(recognitions)
        }
    }

    fun photoUpload(photo: MultipartBody.Part,left: Float, top: Float, right: Float, bottom: Float) {
        _recognitionResponse.postValue(Result.Loading())
        viewModelScope.launch {
            _recognitionResponse.postValue(repository.photoUpload(photo, left, top, right, bottom))
        }
    }
    fun setDataKtp(dataKtp: SetDataKtp) {
        viewModelScope.launch {
            _setDataKtpResponse.postValue(dataKtp)
        }
    }
}


