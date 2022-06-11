package com.safiraak.validin.presentation.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.safiraak.validin.R

class PopUpForgetPass : DialogFragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.container_login)
        return inflater.inflate(R.layout.popup_forgetpass, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val etSetEmail = dialog!!.findViewById<EditText>(R.id.et_FP)
        val buttonConfirm = dialog!!.findViewById<Button>(R.id.button_FP)
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

        etSetEmail.setText(auth.currentUser?.email)
        buttonConfirm.setOnClickListener {
            val emailAddress = etSetEmail.text.trim().toString()
            auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, getString(R.string.reset_pass), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}