package com.renufus.agrohealth.data.model.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("user")
    val user: List<UserItem>,

    @field:SerializedName("status")
    val status: Int,
)
