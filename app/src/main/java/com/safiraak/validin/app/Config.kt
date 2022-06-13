package com.safiraak.validin.app

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


object Config {
    val BASE_URL = "http://35.222.43.223"
    fun getToken(): String {
        val mUser = FirebaseAuth.getInstance().currentUser

        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken: String = task.result.token.toString()
                } else {
                    task.getException()
                }
            }
        return ""
    }
}