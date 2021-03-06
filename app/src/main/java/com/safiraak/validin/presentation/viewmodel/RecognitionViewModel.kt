package com.safiraak.validin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safiraak.validin.data.RecognitionData
import com.safiraak.validin.data.RecognitionRepository
import com.safiraak.validin.data.Result
import com.safiraak.validin.data.remote.CheckDataResponse
import com.safiraak.validin.data.remote.FinalFormResponse
import com.safiraak.validin.data.remote.RecognitionResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RecognitionViewModel @Inject constructor(
    private val repository: RecognitionRepository,
) : ViewModel() {
    private val _recognitionData = repository.recognitionData
    val recognitionData: LiveData<RecognitionData> = _recognitionData
    private val _recognitionResponse = MutableLiveData<Result<RecognitionResponse>>()
    val recognitionResponse: LiveData<Result<RecognitionResponse>> = _recognitionResponse
    private val _finalformResponse = MutableLiveData<Result<FinalFormResponse>>()
    val finalformResponse: LiveData<Result<FinalFormResponse>> = _finalformResponse
    private val _checkdataResponse = MutableLiveData<Result<CheckDataResponse>>()
    val checkdataResponse: LiveData<Result<CheckDataResponse>> = _checkdataResponse


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

    fun finalformUp(provinsi: RequestBody, kota: RequestBody, nik: RequestBody,
                    nama: RequestBody, ttl: RequestBody, jeniskel: RequestBody,
                    alamat: RequestBody, rtrw: RequestBody, keldes: RequestBody,
                    kec: RequestBody, agama: RequestBody, statPer: RequestBody, pekerjaan: RequestBody, kwn: RequestBody
    ) {
        _finalformResponse.postValue(Result.Loading())
        viewModelScope.launch {
            _finalformResponse.postValue(repository.finalformUpload(provinsi, kota, nik, nama, ttl, jeniskel, alamat,
                rtrw, keldes, kec, agama, statPer, pekerjaan, kwn))
        }
    }

    fun checkData() {
        _checkdataResponse.postValue(Result.Loading())
        viewModelScope.launch {
            _checkdataResponse.postValue(repository.checkData())
        }
    }
}


