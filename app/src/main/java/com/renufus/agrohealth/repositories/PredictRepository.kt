package com.renufus.agrohealth.repositories

import com.renufus.agrohealth.data.api.ApiService
import com.renufus.agrohealth.data.model.predictDisease.predictHistory.PredictHistoryResponse
import com.renufus.agrohealth.data.model.predictDisease.predictResult.PredictResponse
import okhttp3.MultipartBody
import org.koin.dsl.module
import retrofit2.Response

val predictRepositoryModule = module {
    factory { PredictRepository(get()) }
}
class PredictRepository(private val apiService: ApiService) {

    suspend fun predictDisease(token: String, image: MultipartBody.Part): Response<PredictResponse> {
        return apiService.predictDisease(token, image)
    }
    suspend fun getPredictHistory(token: String): Response<PredictHistoryResponse> {
        return apiService.getPredictHistory(token)
    }
}
