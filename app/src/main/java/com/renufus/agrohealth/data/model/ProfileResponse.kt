package com.renufus.agrohealth.data.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    @field:SerializedName("data")
    val data: ProfileItem,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Int,
)
