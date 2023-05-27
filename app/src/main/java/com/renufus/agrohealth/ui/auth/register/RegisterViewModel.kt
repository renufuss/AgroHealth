package com.renufus.agrohealth.ui.auth.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.repositories.AuthRepository
import kotlinx.coroutines.launch
import org.koin.dsl.module

val registerViewModelModule = module {
    factory { RegisterViewModel(get()) }
}

class RegisterViewModel(repository: AuthRepository) : ViewModel() {

    private val authRepository = repository
    private val gson = Gson()
    val errorStatus by lazy { MutableLiveData<Boolean>() }
    val register by lazy { MutableLiveData<GeneralResponse>() }

    fun register(email: String, username: String, password: String) {
        viewModelScope.launch {
            val response = authRepository.register(email, username, password)
            if (response.isSuccessful) {
                register.value = response.body()
                errorStatus.value = false
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, GeneralResponse::class.java)
                register.value = errorResponse
                errorStatus.value = true
            }
        }
    }
}
