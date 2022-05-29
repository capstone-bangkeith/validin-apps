package com.safiraak.validin.presentation.view.setting

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.safiraak.validin.R

class PopUpEditPass : DialogFragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.container_login)
        return inflater.inflate(R.layout.popup_edit, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.65).toInt()
        val etEditPass = dialog!!.findViewById<EditText>(R.id.et_change)
        val tvEditPass = dialog!!.findViewById<TextView>(R.id.tv_change)
        val buttonConfirmEdit = dialog!!.findViewById<Button>(R.id.button_change)
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        etEditPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        tvEditPass.text = "Change Your Password?"
        buttonConfirmEdit.setOnClickListener {
            val newPassword = etEditPass.text.trim()
            auth.currentUser?.updatePassword(newPassword.toString())?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Password changed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}