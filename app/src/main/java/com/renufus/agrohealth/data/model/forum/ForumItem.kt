package com.renufus.agrohealth.data.model.forum

import com.google.gson.annotations.SerializedName

data class ForumItem(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("imageUrl")
    val imageUrl: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("modified")
    val modified: String? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("email")
    val email: String? = null,
)
