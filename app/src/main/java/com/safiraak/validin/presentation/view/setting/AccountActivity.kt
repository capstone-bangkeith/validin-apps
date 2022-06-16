package com.safiraak.validin.presentation.view.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.safiraak.validin.R
import com.safiraak.validin.databinding.ActivityAccountBinding
import com.safiraak.validin.presentation.view.auth.LoginActivity
import com.safiraak.validin.presentation.view.main.PopUpUsernameFragment
import com.safiraak.validin.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountActivity : AppCompatActivity(), PopUpUsernameFragment.PopUpUsernameListener{

    private lateinit var binding: ActivityAccountBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel.user.observe(this) {
            if (it == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                binding.apply {
                    uname.text = it.displayName
                    email.text = it.email
                    Glide.with(applicationContext)
                        .load(it.photoUrl)
                        .centerCrop()
                        .error(R.drawable.pierre)
                        .into(imgProfile)
                    buttonEditName.setOnClickListener {
                        val popUpUsernameFragment = PopUpUsernameFragment(getString(R.string.edit_profile_name))
                        popUpUsernameFragment.show(supportFragmentManager,"Pop Up Username")
                    }
                    buttonPass.setOnClickListener {
                        val popupPass = PopUpEditPass()
                        popupPass.show(supportFragmentManager, "Pop Up Password")
                    }
                }
            }
        }
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.accToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getUsername(username: String) {
        userViewModel.editUserName(username)
        binding.uname.text = username
    }
}