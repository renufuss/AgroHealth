package com.renufus.agrohealth.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.renufus.agrohealth.R
import com.renufus.agrohealth.databinding.ActivityMainBinding
import com.renufus.agrohealth.ui.camera.CameraActivity
import com.renufus.agrohealth.utility.GeneralUtility
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val mainModule = module {
    factory { MainActivity() }
}
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val utility = GeneralUtility()
    private val viewModel: MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        utility.setStatusBarColor(this@MainActivity, Color.WHITE)
        binding.floatingActionButtonMainCamera.setOnClickListener {
            utility.moveToAnotherActivity(this@MainActivity, CameraActivity::class.java)
        }

        checkLoginForBottomNav()
    }

    private fun checkLoginForBottomNav() {
        val loginStatus = viewModel.userPreferences.getStatusLogin()
        val navController = findNavController(R.id.nav_host_fragment)
        if (!loginStatus) {
            binding.bottomNavigationMain.visibility = View.INVISIBLE
            binding.bottomNavigationMainUnloggedIn.setupWithNavController(navController)
            binding.bottomNavigationMainUnloggedIn.visibility = View.VISIBLE
        } else {
            binding.bottomNavigationMain.visibility = View.VISIBLE
            binding.bottomNavigationMain.setupWithNavController(navController)
            binding.bottomNavigationMainUnloggedIn.visibility = View.INVISIBLE
        }
    }
}
