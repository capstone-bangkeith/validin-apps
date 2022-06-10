package com.safiraak.validin.data

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class UserRepository @Inject constructor (val application: Application) {

    private val auth = Firebase.auth
    private val _isLogout = MutableLiveData<Boolean>()
    val isLogout : LiveData<Boolean> = _isLogout
    private val _user = MutableLiveData<FirebaseUser>()
    val user : LiveData<FirebaseUser> = _user

    init {
        if (auth.currentUser != null) {
            _user.postValue(auth.currentUser)
            _isLogout.postValue(false)
        }
    }

    fun userRegister(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.postValue(auth.currentUser)
                    _isLogout.postValue(false)
                } else {
                }
            }
    }

    fun userLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.postValue(auth.currentUser)
                    _isLogout.postValue(false)
                } else {
                }
            }
    }

    fun userGoogleLogin(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "SignInWithCredential : Success")
                    _user.postValue(auth.currentUser)
                } else {
                    Log.w(TAG, "SignInWithCredential : Failure", task.exception)
                }
            }
    }

    fun logOut() {
        auth.signOut()
        _isLogout.postValue(true)
    }

    fun editUserName(username: String) {
        val updateUsername = userProfileChangeRequest {
            displayName = username
        }
        auth.currentUser?.updateProfile(updateUsername)?.addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                Toast.makeText(application.applicationContext,"Username saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        val TAG = "UserRepository"
    }
}