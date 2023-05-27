package com.renufus.agrohealth

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.renufus.agrohealth.data.api.networkModule
import com.renufus.agrohealth.repositories.authRepositoryModule
import com.renufus.agrohealth.ui.auth.register.registerModule
import com.renufus.agrohealth.ui.auth.register.registerViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BaseClass : Application() {
    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        startKoin {
            androidLogger()
            androidContext(this@BaseClass)
            modules(
                networkModule,
                authRepositoryModule,
                registerViewModelModule,
                registerModule,
            )
        }
    }
}
