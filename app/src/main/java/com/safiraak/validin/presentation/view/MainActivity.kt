package com.safiraak.validin.presentation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.safiraak.validin.R
import com.safiraak.validin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), PopUpUsernameFragment.PopUpUsernameListener {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //FirebaseAuth
        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        setSupportActionBar(binding.mainAppBar.mainToolbar)

        toggle = ActionBarDrawerToggle(this, binding.homeDrawerAct, R.string.open, R.string.close)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.black)
        binding.homeDrawerAct.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.navView.itemIconTintList = null
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_account -> startActivity(Intent(this, AccountActivity::class.java))
                R.id.nav_setting -> startActivity(Intent(this, SettingActivity::class.java))
                R.id.nav_user_feedback -> showMessage("Feedback Clicked")
                R.id.logout -> { logOut() }
            }
            true
        }


        if (firebaseUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        binding.apply {
            mainAppBar.tvUname.text = firebaseUser.displayName
            navView.getHeaderView(0).findViewById<TextView>(R.id.uname).text = firebaseUser.displayName
            navView.getHeaderView(0).findViewById<TextView>(R.id.email).text = firebaseUser.email
            Glide.with(applicationContext)
                .load(firebaseUser.photoUrl)
                .centerCrop()
                .error(R.drawable.pierre)
                .into(mainAppBar.imgBaseprofile)
            Glide.with(applicationContext)
                .load(firebaseUser.photoUrl)
                .centerCrop()
                .error(R.drawable.pierre)
                .into(navView.getHeaderView(0).findViewById<ImageView>(R.id.img_profile))
        }
    }

    override fun onStart() {
        super.onStart()
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        if (firebaseUser.displayName == null || firebaseUser.displayName!!.isEmpty()){
            val popUpUsernameFragment = PopUpUsernameFragment()
            popUpUsernameFragment.show(supportFragmentManager,"Pop Up Username")
        }
    }

    private fun logOut() {
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun getUsername(username: String) {
        val firebaseUser = auth.currentUser
        val updateUsername = userProfileChangeRequest {
            displayName = username
        }
        firebaseUser?.updateProfile(updateUsername)?.addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext,"Username saved", Toast.LENGTH_SHORT).show()
            }
        }
        binding.mainAppBar.tvUname.text = username
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.uname).text = username
    }
}