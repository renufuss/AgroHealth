package com.renufus.agrohealth.ui.main

import androidx.lifecycle.ViewModel
import com.renufus.agrohealth.data.preferences.UserPreferences
import org.koin.dsl.module

val mainViewModelModule = module {
    factory { MainViewModel(get()) }
}
class MainViewModel(val userPreferences: UserPreferences) : ViewModel()
