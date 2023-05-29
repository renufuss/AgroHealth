package com.renufus.agrohealth.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.LoginResponse
import com.renufus.agrohealth.data.preferences.UserPreferences
import com.renufus.agrohealth.repositories.AuthRepository
import com.renufus.agrohealth.utility.SingleEventLiveData
import kotlinx.coroutines.launch
import org.koin.dsl.module

val loginViewModelModule = module {
    factory { LoginViewModel(get(), get()) }
}
class LoginViewModel(private val repository: AuthRepository, val userPreferences: UserPreferences) : ViewModel() {
    private val gson = Gson()
    val errorStatus by lazy { SingleEventLiveData<Boolean>() }
    val errorMessage by lazy { SingleEventLiveData<GeneralResponse>() }
    val login by lazy { SingleEventLiveData<LoginResponse>() }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = repository.login(email, password)
            if (response.isSuccessful) {
                login.setValue(response.body()!!)
                errorStatus.setValue(false)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, GeneralResponse::class.java)
                errorMessage.setValue(errorResponse)
                errorStatus.setValue(true)
            }
        }
    }
}
