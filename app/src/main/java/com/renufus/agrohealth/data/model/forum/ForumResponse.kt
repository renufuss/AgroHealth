package com.renufus.agrohealth.data.model.forum

import com.google.gson.annotations.SerializedName

data class ForumResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("allPost")
    val allPost: List<ForumItem>,

    @field:SerializedName("status")
    val status: Int? = null,
)
