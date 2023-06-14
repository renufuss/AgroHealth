package com.renufus.agrohealth.data.api

import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.articles.ArticlesResponse
import com.renufus.agrohealth.data.model.auth.LoginRequest
import com.renufus.agrohealth.data.model.auth.LoginResponse
import com.renufus.agrohealth.data.model.auth.ProfileResponse
import com.renufus.agrohealth.data.model.auth.RefreshTokenResponse
import com.renufus.agrohealth.data.model.auth.RegistrationRequest
import com.renufus.agrohealth.data.model.forum.ForumResponse
import com.renufus.agrohealth.data.model.predict.PredictResponse
import com.renufus.agrohealth.data.model.predict.predictHistory.PredictHistoryResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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

    @Multipart
    @POST("api/predict")
    suspend fun predictDisease(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
    ): Response<PredictResponse>

    @GET("api/forum/posts")
    suspend fun getForumContent(): Response<ForumResponse>

    @GET("historyPredicts")
    suspend fun getPredictHistory(): Response<PredictHistoryResponse>
}
