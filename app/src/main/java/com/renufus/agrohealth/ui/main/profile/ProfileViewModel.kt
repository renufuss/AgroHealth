package com.renufus.agrohealth.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.auth.ProfileResponse
import com.renufus.agrohealth.data.preferences.UserPreferences
import com.renufus.agrohealth.repositories.UserRepository
import com.renufus.agrohealth.utility.SingleEventLiveData
import kotlinx.coroutines.launch
import okio.IOException
import org.koin.dsl.module
import java.lang.Exception

val profileViewModelModule = module {
    factory { ProfileViewModel(get(), get()) }
}
class ProfileViewModel(private val repository: UserRepository, val userPreferences: UserPreferences) : ViewModel() {
    private val gson = Gson()
    val errorTokenStatus by lazy { SingleEventLiveData<Boolean>() }
    val errorStatus by lazy { SingleEventLiveData<Boolean>() }
    val errorMessage by lazy { SingleEventLiveData<String>() }
    val profile by lazy { SingleEventLiveData<ProfileResponse>() }

    fun getProfile() {
        viewModelScope.launch {
            try {
                val response = repository.getProfile(userPreferences.getToken()!!)

                if (response.isSuccessful) {
                    profile.setValue(response.body()!!)
                    errorStatus.setValue(false)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = gson.fromJson(errorBody, GeneralResponse::class.java)
                    errorMessage.setValue(errorResponse.message)
                    errorStatus.setValue(true)
                }
            } catch (e: Exception) {
                errorStatus.setValue(true)
                errorTokenStatus.setValue(true)
                errorMessage.setValue("A network problem occurred")
            }
        }
    }

    fun refreshToken() {
        viewModelScope.launch {
            try {
                val response = repository.refreshToken(userPreferences.getToken()!!)

                if (response.isSuccessful) {
                    userPreferences.setToken(response.body()!!.refreshToken)
                    errorTokenStatus.setValue(false)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = gson.fromJson(errorBody, GeneralResponse::class.java)
                    errorMessage.setValue(errorResponse.message)
                    errorTokenStatus.setValue(true)
                }
            } catch (e: Exception) {
                errorStatus.setValue(true)
                when (e) {
                    is IOException -> {
                        errorMessage.setValue("A network problem occurred")
                        errorTokenStatus.setValue(false)
                    }
                    else -> {
                        errorTokenStatus.setValue(true)
                        errorMessage.setValue("Your token is expired")
                    }
                }
            }
        }
    }
}
