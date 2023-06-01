package com.renufus.agrohealth.data.api

import com.renufus.agrohealth.data.model.articles.ArticlesResponse
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.auth.LoginRequest
import com.renufus.agrohealth.data.model.auth.LoginResponse
import com.renufus.agrohealth.data.model.auth.ProfileResponse
import com.renufus.agrohealth.data.model.auth.RefreshTokenResponse
import com.renufus.agrohealth.data.model.auth.RegistrationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("api/user/login")
    suspend fun login(@Body requestBody: LoginRequest): Response<LoginResponse>

    @POST("api/user/register")
    suspend fun register(@Body requestBody: RegistrationRequest): Response<GeneralResponse>

    @GET("api/user/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String,
    ): Response<ProfileResponse>

    @GET("api/user/refreshtoken")
    suspend fun refreshToken(
        @Header("Authorization") token: String,
    ): Response<RefreshTokenResponse>

    @GET("api/articles")
    suspend fun getArticles(): Response<ArticlesResponse>
}
