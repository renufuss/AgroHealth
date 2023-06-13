package com.renufus.agrohealth.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import org.koin.dsl.module

val userPreferenceModule = module {
    factory { UserPreferences(get()) }
}

class UserPreferences(context: Context) {
    val loginStatusKey = "loginStatus"
    val tokenApiKey = "tokenApi"
    val sharedPreference: SharedPreferences

    init {
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setStatusLogin(status: Boolean) {
        sharedPreference.edit().putBoolean(loginStatusKey, status).apply()
    }

    fun setToken(token: String?) {
        sharedPreference.edit().putString(tokenApiKey, "Bearer $token").apply()
    }

    fun getStatusLogin(): Boolean {
        return sharedPreference.getBoolean(loginStatusKey, false)
    }

    fun getToken(): String? {
        return sharedPreference.getString(tokenApiKey, null)
    }
}
