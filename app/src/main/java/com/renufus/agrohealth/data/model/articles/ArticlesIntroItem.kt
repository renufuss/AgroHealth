package com.renufus.agrohealth.data.model.articles

import com.google.gson.annotations.SerializedName

data class ArticlesIntroItem(

    @field:SerializedName("data")
    val data: List<ArticlesItem>,

    @field:SerializedName("length")
    val length: Int,
)
