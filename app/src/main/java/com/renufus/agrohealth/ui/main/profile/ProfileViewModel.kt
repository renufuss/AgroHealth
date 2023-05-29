package com.renufus.agrohealth.ui.main.profile

import androidx.lifecycle.ViewModel
import com.renufus.agrohealth.data.preferences.UserPreferences
import org.koin.dsl.module

val profileViewModelModule = module {
    factory { ProfileViewModel(get()) }
}
class ProfileViewModel(val userPreferences: UserPreferences) : ViewModel()
