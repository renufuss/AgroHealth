package com.renufus.agrohealth.ui.camera

import androidx.lifecycle.ViewModel
import com.renufus.agrohealth.data.preferences.UserPreferences
import org.koin.dsl.module

val cameraViewModelModule = module {
    factory { CameraViewModel(get()) }
}
class CameraViewModel(val userPreferences: UserPreferences) : ViewModel()
