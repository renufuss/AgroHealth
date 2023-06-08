package com.renufus.agrohealth.ui.predictDisease

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.predict.PredictResponse
import com.renufus.agrohealth.repositories.PredictRepository
import com.renufus.agrohealth.utility.SingleEventLiveData
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.koin.dsl.module
import java.lang.Exception

val predictDiseaseViewModelModule = module {
    factory { PredictDiseaseViewModel(get()) }
}
class PredictDiseaseViewModel(private val repository: PredictRepository) : ViewModel() {
    private val gson = Gson()
    val errorStatus by lazy { SingleEventLiveData<Boolean>() }
    val errorMessage by lazy { SingleEventLiveData<String>() }
    val prediction by lazy { MutableLiveData<PredictResponse>() }

    fun predictDisease(image: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val response = repository.predictDisease(image)

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
                errorMessage.setValue(e.message.toString())
            }
        }
    }
}
