package com.renufus.agrohealth.data.model

import com.google.gson.annotations.SerializedName

data class UserItem(

    @field:SerializedName("user_id")
    val userId: Int,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("token")
    val token: String,
)
