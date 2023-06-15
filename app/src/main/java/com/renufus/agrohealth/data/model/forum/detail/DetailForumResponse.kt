package com.renufus.agrohealth.data.model.forum.detail

import com.google.gson.annotations.SerializedName

data class DetailForumResponse(

    @field:SerializedName("postById")
    val postById: DetailForumItem? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null,
)
