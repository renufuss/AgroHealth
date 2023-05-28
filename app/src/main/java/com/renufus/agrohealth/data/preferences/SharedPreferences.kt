package com.renufus.agrohealth.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import org.koin.dsl.module

val sharedPreferenceModule = module {
    factory { SharedPreferences(get()) }
}

class SharedPreferences(context: Context) {
    val login = "login"
    var tokenApi: String? = null
    val sharedPreference: SharedPreferences

    init {
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setStatusLogin(status: Boolean) {
        sharedPreference.edit().putBoolean(login, status).apply()
    }

    fun setToken(token: String?) {
        sharedPreference.edit().putString(tokenApi, token).apply()
    }

    fun getStatusLogin(): Boolean {
        return sharedPreference.getBoolean(login, false)
    }

    fun getToken(): String? {
        return sharedPreference.getString(tokenApi, null)
    }
}
