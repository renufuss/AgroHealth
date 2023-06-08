package com.renufus.agrohealth.repositories

import com.renufus.agrohealth.data.api.ApiService
import com.renufus.agrohealth.data.model.predict.predictHistory.PredictHistoryResponse
import org.koin.dsl.module
import retrofit2.Response

val predictRepositoryModule = module {
    factory { PredictRepository(get()) }
}
class PredictRepository(private val apiService: ApiService) {
    suspend fun getPredictHistory(): Response<PredictHistoryResponse> {
        return apiService.getPredictHistory()
    }
}