package com.renufus.agrohealth.data.api

import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.LoginResponse
import com.renufus.agrohealth.data.model.RegistrationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @POST("api/user/register")
    suspend fun register(@Body requestBody: RegistrationRequest): Response<GeneralResponse>
}
