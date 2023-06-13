package com.renufus.agrohealth.ui.splash

import androidx.lifecycle.ViewModel
import com.renufus.agrohealth.data.preferences.UserPreferences
import org.koin.dsl.module

val splashViewModelModule = module {
    factory { SplashViewModel(get()) }
}

class SplashViewModel(val userPreferences: UserPreferences) : ViewModel()
