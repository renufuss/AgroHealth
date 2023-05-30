package com.renufus.agrohealth.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.repositories.UserRepository
import com.renufus.agrohealth.utility.SingleEventLiveData
import kotlinx.coroutines.launch
import org.koin.dsl.module

val registerViewModelModule = module {
    factory { RegisterViewModel(get()) }
}

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    private val gson = Gson()
    val errorStatus by lazy { SingleEventLiveData<Boolean>() }
    val register by lazy { SingleEventLiveData<GeneralResponse>() }

    fun register(email: String, username: String, password: String) {
        viewModelScope.launch {
            val response = repository.register(email, username, password)
            if (response.isSuccessful) {
                register.setValue(response.body()!!)
                errorStatus.setValue(false)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, GeneralResponse::class.java)
                register.setValue(errorResponse)
                errorStatus.setValue(true)
            }
        }
    }
}
