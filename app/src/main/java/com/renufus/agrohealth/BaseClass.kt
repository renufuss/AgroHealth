package com.renufus.agrohealth

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.renufus.agrohealth.data.api.networkModule
import com.renufus.agrohealth.data.preferences.userPreferenceModule
import com.renufus.agrohealth.repositories.articleRepositoryModule
import com.renufus.agrohealth.repositories.forumRepositoryModule
import com.renufus.agrohealth.repositories.predictRepositoryModule
import com.renufus.agrohealth.repositories.userRepositoryModule
import com.renufus.agrohealth.ui.auth.login.loginModule
import com.renufus.agrohealth.ui.auth.login.loginViewModelModule
import com.renufus.agrohealth.ui.auth.register.registerModule
import com.renufus.agrohealth.ui.auth.register.registerViewModelModule
import com.renufus.agrohealth.ui.main.articles.articleModule
import com.renufus.agrohealth.ui.main.articles.articleViewModelModule
import com.renufus.agrohealth.ui.main.forum.forumModule
import com.renufus.agrohealth.ui.main.forum.forumViewModelModule
import com.renufus.agrohealth.ui.main.history.historyModule
import com.renufus.agrohealth.ui.main.history.historyViewModelModule
import com.renufus.agrohealth.ui.main.profile.profileModule
import com.renufus.agrohealth.ui.main.profile.profileViewModelModule
import com.renufus.agrohealth.ui.predictDisease.predictDiseaseViewModelModule
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
                userRepositoryModule,
                registerViewModelModule,
                registerModule,
                loginViewModelModule,
                loginModule,
                userPreferenceModule,
                profileViewModelModule,
                profileModule,
                articleRepositoryModule,
                articleViewModelModule,
                articleModule,
                forumRepositoryModule,
                forumViewModelModule,
                forumModule,
                predictRepositoryModule,
                historyViewModelModule,
                historyModule,
                predictDiseaseViewModelModule,
            )
        }
    }
}
