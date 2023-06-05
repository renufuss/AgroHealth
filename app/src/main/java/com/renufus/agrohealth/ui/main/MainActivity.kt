package com.renufus.agrohealth.ui.main

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.renufus.agrohealth.R
import com.renufus.agrohealth.databinding.ActivityMainBinding
import com.renufus.agrohealth.ui.camera.CameraActivity
import com.renufus.agrohealth.utility.GeneralUtility

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val utility = GeneralUtility()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigationMain.setupWithNavController(navController)

        utility.setStatusBarColor(this@MainActivity, Color.WHITE)
        binding.floatingActionButtonMainCamera.setOnClickListener {
            utility.moveToAnotherActivity(this@MainActivity, CameraActivity::class.java)
        }
    }
}
