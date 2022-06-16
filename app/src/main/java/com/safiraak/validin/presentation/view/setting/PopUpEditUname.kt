package com.safiraak.validin.presentation.view.setting

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
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.safiraak.validin.R

class PopUpEditUname : DialogFragment() {

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
        val etEditUname = dialog!!.findViewById<EditText>(R.id.et_change)
        val buttonConfirmEdit = dialog!!.findViewById<Button>(R.id.button_change)
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

        etEditUname.setText(auth.currentUser?.displayName)
        buttonConfirmEdit.setOnClickListener {
            val newUname = etEditUname.text.trim()
            val updates = userProfileChangeRequest { displayName = newUname.toString() }
            auth.currentUser?.updateProfile(updates)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, getString(R.string.done_cg_uname), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}