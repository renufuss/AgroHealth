package com.renufus.agrohealth.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.ProfileResponse
import com.renufus.agrohealth.data.preferences.UserPreferences
import com.renufus.agrohealth.repositories.UserRepository
import com.renufus.agrohealth.utility.SingleEventLiveData
import kotlinx.coroutines.launch
import org.koin.dsl.module

val profileViewModelModule = module {
    factory { ProfileViewModel(get(), get()) }
}
class ProfileViewModel(private val repository: UserRepository, val userPreferences: UserPreferences) : ViewModel() {
    private val gson = Gson()
    val errorTokenStatus by lazy { SingleEventLiveData<Boolean>() }
    val errorStatus by lazy { SingleEventLiveData<Boolean>() }
    val errorMessage by lazy { SingleEventLiveData<GeneralResponse>() }
    val profile by lazy { SingleEventLiveData<ProfileResponse>() }

    fun getProfile() {
        viewModelScope.launch {
            val response = repository.getProfile(userPreferences.getToken()!!)

            if (response.isSuccessful) {
                profile.setValue(response.body()!!)
                errorStatus.setValue(false)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, GeneralResponse::class.java)
                errorMessage.setValue(errorResponse)
                errorStatus.setValue(true)
            }
        }
    }

    fun refreshToken() {
        viewModelScope.launch {
            val response = repository.refreshToken(userPreferences.getToken()!!)

            if (response.isSuccessful) {
                userPreferences.setToken(response.body()!!.refreshToken)
                errorTokenStatus.setValue(false)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, GeneralResponse::class.java)
                errorMessage.setValue(errorResponse)
                errorTokenStatus.setValue(true)
            }
        }
    }
}
