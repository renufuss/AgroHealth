package com.renufus.agrohealth.repositories

import com.renufus.agrohealth.data.api.ApiService
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.LoginRequest
import com.renufus.agrohealth.data.model.LoginResponse
import com.renufus.agrohealth.data.model.RegistrationRequest
import org.koin.dsl.module
import retrofit2.Response

val authRepositoryModule = module {
    factory { AuthRepository(get()) }
}

class AuthRepository(
    private val apiService: ApiService,
) {
    suspend fun login(email: String, password: String): Response<LoginResponse> {
        val requestBody = LoginRequest(email, password)
        return apiService.login(requestBody)
    }

    suspend fun register(email: String, username: String, password: String): Response<GeneralResponse> {
        val requestBody = RegistrationRequest(email, username, password)
        return apiService.register(requestBody)
    }
}
