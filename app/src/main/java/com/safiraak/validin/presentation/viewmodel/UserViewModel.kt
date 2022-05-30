package com.safiraak.validin.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.safiraak.validin.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    val application: Application,
    private val repository: UserRepository
) : ViewModel() {

    private var _user = repository._user
    val user : LiveData<FirebaseUser> = _user
    private var _isLogout = repository._isLogout
    val isLogout : LiveData<Boolean> = _isLogout

    fun userLogin (email: String, password: String) {
        viewModelScope.launch {
            return@launch repository.userLogin(email, password)
        }
    }

    fun userRegister (email: String, password: String) {
        viewModelScope.launch {
            return@launch repository.userRegister(email, password)
        }
    }

    fun userLoginGoogle(idToken: String) {
        viewModelScope.launch {
            return@launch repository.userGoogleLogin(idToken)
        }
    }

    fun userLogout() {
        viewModelScope.launch {
            return@launch repository.logOut()
        }
    }

    fun editUserName(username: String) {
        viewModelScope.launch {
            return@launch repository.editUserName(username)
        }
    }
}