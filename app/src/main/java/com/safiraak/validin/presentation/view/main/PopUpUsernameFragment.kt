package com.safiraak.validin.presentation.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.safiraak.validin.databinding.FragmentPopUpUsernameBinding


class PopUpUsernameFragment : AppCompatDialogFragment() {
    private var listener: PopUpUsernameListener? = null

    private var _binding: FragmentPopUpUsernameBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopUpUsernameBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSaveUsername.setOnClickListener {
            val username = binding.pwUsernameField.text.toString().trim()
            listener?.getUsername(username)
            dialog?.dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as PopUpUsernameListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString() + "not implemented"
            )
        }
    }

    interface PopUpUsernameListener {
        fun getUsername(username: String)
    }
}