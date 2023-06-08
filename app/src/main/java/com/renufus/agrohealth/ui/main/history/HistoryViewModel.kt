package com.renufus.agrohealth.ui.main.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.predict.predictHistory.PredictHistoryResponse
import com.renufus.agrohealth.repositories.PredictRepository
import com.renufus.agrohealth.utility.SingleEventLiveData
import kotlinx.coroutines.launch
import org.koin.dsl.module
import java.lang.Exception

val historyViewModelModule = module {
    factory { HistoryViewModel(get()) }
}
class HistoryViewModel(private val repository: PredictRepository) : ViewModel() {
    private val gson = Gson()
    val errorStatus by lazy { SingleEventLiveData<Boolean>() }
    val errorMessage by lazy { SingleEventLiveData<String>() }
    val predictHistory by lazy { MutableLiveData<PredictHistoryResponse>() }

    fun getHistory() {
        viewModelScope.launch {
            try {
                val response = repository.getPredictHistory()

                if (response.isSuccessful) {
                    predictHistory.value = response.body()!!
                    errorStatus.setValue(false)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = gson.fromJson(errorBody, GeneralResponse::class.java)
                    errorMessage.setValue(errorResponse.message)
                    errorStatus.setValue(true)
                }
            } catch (e: Exception) {
                errorStatus.setValue(true)
                errorMessage.setValue("A network problem occurred")
            }
        }
    }
}
