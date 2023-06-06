package com.renufus.agrohealth.data.model.forum

import com.google.gson.annotations.SerializedName

data class ForumItem(

    @field:SerializedName("contentImage")
    val contentImage: String? = null,

    @field:SerializedName("publishedAt")
    val publishedAt: String? = null,

    @field:SerializedName("author")
    val author: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("commentCounter")
    val commentCounter: Int? = null,

    @field:SerializedName("content")
    val content: String? = null,

    @field:SerializedName("likeStatus")
    val likeStatus: Boolean? = null,

    @field:SerializedName("likeCounter")
    val likeCounter: Int? = null,
)
