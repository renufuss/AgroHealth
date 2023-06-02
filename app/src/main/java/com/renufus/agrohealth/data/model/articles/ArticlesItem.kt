package com.renufus.agrohealth.data.model.articles

import com.google.gson.annotations.SerializedName

data class ArticlesItem(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("gambar")
    val gambar: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("_id")
    val _id: String,
)
