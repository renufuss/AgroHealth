package com.renufus.agrohealth.data.model.articles

import com.google.gson.annotations.SerializedName

data class ArticlesFarmingItem(

    @field:SerializedName("data")
	val data: List<ArticlesItem?>? = null,

    @field:SerializedName("length")
	val length: Int? = null
)