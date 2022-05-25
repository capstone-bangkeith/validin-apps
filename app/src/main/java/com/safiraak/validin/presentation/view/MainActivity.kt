package com.safiraak.validin.presentation.view

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.safiraak.validin.R
import com.safiraak.validin.databinding.ActivityMainBinding
import com.safiraak.validin.utils.CamUtils
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainAppBar.mainToolbar)

        toggle = ActionBarDrawerToggle(this, binding.homeDrawerAct, R.string.open, R.string.close)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.black)
        binding.homeDrawerAct.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.mainAppBar.cardCam.setOnClickListener{
            cameraXGo()
        }

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

        //FirebaseAuth
        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
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

    private fun cameraXGo() {
        val intent = Intent(this, CameraxActivity::class.java)
        cameraLauncher.launch(intent)
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == CAMERAX_RESULT) {
            val file = it.data?.getSerializableExtra("picture") as File
            val backCam = it.data?.getBooleanExtra("backCam", true) as Boolean
            val result = CamUtils().rotateBitmap(
                BitmapFactory.decodeFile(file.path),
                backCam
            )
            val backFile = CamUtils().bitmap2File(this, result)
            getFile = backFile
        }
    }

    companion object {
        const val CAMERAX_RESULT = 200
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}