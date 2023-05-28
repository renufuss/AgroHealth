package com.renufus.agrohealth.ui.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.LoginResponse
import com.renufus.agrohealth.data.preferences.SharedPreferences
import com.renufus.agrohealth.repositories.AuthRepository
import kotlinx.coroutines.launch
import org.koin.dsl.module

val loginViewModelModule = module {
    factory { LoginViewModel(get(), get()) }
}
class LoginViewModel(private val repository: AuthRepository, val sharedPreferences: SharedPreferences) : ViewModel()
{
    private val gson = Gson()
    val errorStatus by lazy { MutableLiveData<Boolean>() }
    val errorMessage by lazy { MutableLiveData<GeneralResponse>() }
    val login by lazy { MutableLiveData<LoginResponse>() }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = repository.login(email, password)
            if (response.isSuccessful) {
                login.value = response.body()
                errorStatus.value = false
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, GeneralResponse::class.java)
                errorMessage.value = errorResponse
                errorStatus.value = true
            }
        }
    }
}