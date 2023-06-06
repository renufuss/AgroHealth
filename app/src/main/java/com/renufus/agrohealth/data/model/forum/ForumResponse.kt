package com.renufus.agrohealth.data.model.forum

import com.google.gson.annotations.SerializedName

data class ForumResponse(

    @field:SerializedName("totalResult")
    val totalResult: Int? = null,

    @field:SerializedName("data")
    val data: List<ForumItem>,

    @field:SerializedName("status")
    val status: Int? = null,
)
