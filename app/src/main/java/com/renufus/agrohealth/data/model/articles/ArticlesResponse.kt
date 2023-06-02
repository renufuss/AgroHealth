package com.renufus.agrohealth.data.model.articles

import com.google.gson.annotations.SerializedName

data class ArticlesResponse(

    @field:SerializedName("total")
    val total: Int,

    @field:SerializedName("data")
    val data: List<ArticlesCategory>,
)
