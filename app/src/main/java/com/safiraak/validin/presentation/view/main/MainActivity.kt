package com.safiraak.validin.presentation.view.main

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.safiraak.validin.R
import com.safiraak.validin.data.Result
import com.safiraak.validin.databinding.ActivityMainBinding
import com.safiraak.validin.presentation.view.setting.AccountActivity
import com.safiraak.validin.presentation.view.setting.SettingActivity
import com.safiraak.validin.presentation.view.auth.LoginActivity
import com.safiraak.validin.presentation.view.verify.CameraTempActivity
import com.safiraak.validin.presentation.viewmodel.RecognitionViewModel
import com.safiraak.validin.presentation.viewmodel.ThemeViewModel
import com.safiraak.validin.presentation.viewmodel.ThemeViewModelFactory
import com.safiraak.validin.presentation.viewmodel.UserViewModel
import com.safiraak.validin.utils.CamUtils
import com.safiraak.validin.utils.ThemePreference
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "themes")

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PopUpUsernameFragment.PopUpUsernameListener {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var getFile: File? = null
    private val userViewModel: UserViewModel by viewModels()
    private val recogViewModel: RecognitionViewModel by viewModels()
    private var verifiedState: Boolean? = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel.getIdTokenForUser()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //FirebaseAuth
        auth = Firebase.auth
        setSupportActionBar(binding.mainAppBar.mainToolbar)

        toggle = ActionBarDrawerToggle(this, binding.homeDrawerAct, R.string.open, R.string.close)
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(applicationContext, R.color.black)
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

        getThemeS()

        binding.mainAppBar.cardInsctruction.setOnClickListener { startActivity(Intent(this, TutorialActivity::class.java)) }

        binding.navView.itemIconTintList = null
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_account -> startActivity(Intent(this, AccountActivity::class.java))
                R.id.nav_setting -> startActivity(Intent(this, SettingActivity::class.java))
                R.id.nav_user_feedback -> showMessage(getString(R.string.feedback_click))
                R.id.logout -> { logOut() }
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser?.displayName == null || auth.currentUser?.displayName!!.isEmpty()){
            val popUpUsernameFragment = PopUpUsernameFragment(getString(R.string.input_username_message))
            popUpUsernameFragment.show(supportFragmentManager,"Pop Up Username")
        }
        recogViewModel.checkData()
        recogViewModel.checkdataResponse.observe(this){
            when(it){
                is Result.Success -> {
                    if (it.data?.data?.validated == true){
                        verifiedState = true
                    } else if (it.data?.data?.validated == false) {
                        verifiedState = false
                    }
                    verifyState()
                    Log.d("NIK", it.data?.data?.nik.toString())
                }
                is Result.Error -> {
                    verifiedState = false
                    verifyState()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        finish()
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
                        .into(navView.getHeaderView(0).findViewById(R.id.img_profile))
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
        val intent = Intent(this, CameraTempActivity::class.java)
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

    fun getThemeS() {
        val pref = ThemePreference.getInstance(dataStore)
        val themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref)).get(
            ThemeViewModel::class.java
        )
        themeViewModel.getThemeSet().observe(this) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    fun verifyState() {
        if (verifiedState == true) {
            binding.mainAppBar.llVerifStat.setBackgroundResource(R.drawable.rounded_verified_label)
            binding.mainAppBar.tvVerifStat.text = getString(R.string.verify_status0)
            binding.mainAppBar.tvTitleOcr.text = getString(R.string.title_check_data)
            binding.mainAppBar.cardCam.setOnClickListener{
                startActivity(Intent(this, CheckDataActivity::class.java))
            }
        } else if (verifiedState == false) {
            binding.mainAppBar.cardCam.setOnClickListener{
                cameraXGo()
            }
        }
    }

    override fun getUsername(username: String) {
        userViewModel.editUserName(username)
        binding.mainAppBar.tvUname.text = username
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.uname).text = username
    }
}