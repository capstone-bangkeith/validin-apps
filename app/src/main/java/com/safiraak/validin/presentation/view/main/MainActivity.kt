package com.safiraak.validin.presentation.view.main

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.safiraak.validin.R
import com.safiraak.validin.databinding.ActivityMainBinding
import com.safiraak.validin.presentation.view.setting.AccountActivity
import com.safiraak.validin.presentation.view.verify.CameraxActivity
import com.safiraak.validin.presentation.view.setting.SettingActivity
import com.safiraak.validin.presentation.view.auth.LoginActivity
import com.safiraak.validin.presentation.viewmodel.UserViewModel
import com.safiraak.validin.utils.CamUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PopUpUsernameFragment.PopUpUsernameListener {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var getFile: File? = null
    private val userViewModel: UserViewModel by viewModels()


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

        userViewModel.user.observe(this) { user ->
            if (user != null) {
                binding.apply {
                    mainAppBar.tvUname.text = user.displayName
                    navView.getHeaderView(0).findViewById<TextView>(R.id.uname).text = user.displayName
                    navView.getHeaderView(0).findViewById<TextView>(R.id.email).text = user.email
                    Glide.with(applicationContext)
                        .load(user.photoUrl)
                        .centerCrop()
                        .error(R.drawable.pierre)
                        .into(mainAppBar.imgBaseprofile)
                    Glide.with(applicationContext)
                        .load(user.photoUrl)
                        .centerCrop()
                        .error(R.drawable.pierre)
                        .into(navView.getHeaderView(0).findViewById<ImageView>(R.id.img_profile))
                }
            }
        }
        userViewModel.isLogout.observe(this) {
            if (it) {
                startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.mainAppBar.cardCam.setOnClickListener{
            cameraXGo()
        }
        
        binding.mainAppBar.cardInsctruction.setOnClickListener { startActivity(Intent(this, TutorialActivity::class.java)) }

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
    }

    override fun onResume() {
        super.onResume()
        userViewModel.user.observe(this) { user ->
            if (user != null) {
                binding.apply {
                    mainAppBar.tvUname.text = user.displayName
                    navView.getHeaderView(0).findViewById<TextView>(R.id.uname).text = user.displayName
                    navView.getHeaderView(0).findViewById<TextView>(R.id.email).text = user.email
                    Glide.with(applicationContext)
                        .load(user.photoUrl)
                        .centerCrop()
                        .error(R.drawable.pierre)
                        .into(mainAppBar.imgBaseprofile)
                    Glide.with(applicationContext)
                        .load(user.photoUrl)
                        .centerCrop()
                        .error(R.drawable.pierre)
                        .into(navView.getHeaderView(0).findViewById<ImageView>(R.id.img_profile))
                }
            }
        }
        userViewModel.isLogout.observe(this) {
            if (it) {
                startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser?.displayName == null || auth.currentUser?.displayName!!.isEmpty()){
            val popUpUsernameFragment = PopUpUsernameFragment(getString(R.string.input_username_message))
            popUpUsernameFragment.show(supportFragmentManager,"Pop Up Username")
        }
    }

    private fun logOut() {
        userViewModel.userLogout()
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

    override fun getUsername(username: String) {
        userViewModel.editUserName(username)
        binding.mainAppBar.tvUname.text = username
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.uname).text = username
    }
}