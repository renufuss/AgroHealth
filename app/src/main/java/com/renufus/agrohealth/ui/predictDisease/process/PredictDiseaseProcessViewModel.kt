package com.renufus.agrohealth.ui.predictDisease.process

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.predictDisease.predictResult.PredictResponse
import com.renufus.agrohealth.data.preferences.UserPreferences
import com.renufus.agrohealth.repositories.PredictRepository
import com.renufus.agrohealth.repositories.UserRepository
import com.renufus.agrohealth.utility.SingleEventLiveData
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okio.IOException
import org.koin.dsl.module
import kotlin.Exception

val predictDiseaseViewModelModule = module {
    factory { PredictDiseaseViewModel(get(), get(), get()) }
}
class PredictDiseaseViewModel(private val repository: PredictRepository, val userPreferences: UserPreferences, private val userRepository: UserRepository) : ViewModel() {
    private val gson = Gson()
    val errorTokenStatus by lazy { SingleEventLiveData<Boolean>() }
    val errorStatus by lazy { SingleEventLiveData<Boolean>() }
    val errorMessage by lazy { SingleEventLiveData<String>() }
    val prediction by lazy { MutableLiveData<PredictResponse>() }

    fun predictDisease(image: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val response = repository.predictDisease(userPreferences.getToken()!!, image)

                if (response.isSuccessful) {
                    prediction.value = response.body()!!
                    errorStatus.setValue(false)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = gson.fromJson(errorBody, GeneralResponse::class.java)
                    errorMessage.setValue(errorResponse.message)
                    errorStatus.setValue(true)
                }
            } catch (e: Exception) {
                errorStatus.setValue(true)
                errorStatus.setValue(true)
                when (e) {
                    is IOException -> {
                        errorMessage.setValue("A network problem occurred")
                    }
                    else -> {
                        errorMessage.setValue("Umm sorry, we can't detect your image")
                    }
                }
            }
        }
    }

    fun refreshToken() {
        viewModelScope.launch {
            try {
                val response = userRepository.refreshToken(userPreferences.getToken()!!)

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
                errorTokenStatus.setValue(true)
                errorMessage.setValue("Your token is expired, Make sure you only log in on 1 device")
            }
        }
    }
}
