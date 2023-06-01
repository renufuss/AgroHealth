package com.renufus.agrohealth.data.model.articles

import com.google.gson.annotations.SerializedName

data class ArticlesItem(

    @field:SerializedName("articles_farming")
    val articlesFarming: List<ArticlesFarmingItem?>? = null,

    @field:SerializedName("articles_intro")
    val articlesIntro: List<ArticlesIntroItem?>? = null,

    @field:SerializedName("articles_business")
    val articlesBusiness: List<ArticlesBusinessItem?>? = null,

    @field:SerializedName("articles_history")
    val articlesHistory: List<ArticlesHistoryItem?>? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("_id")
    val _id: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("gambar")
    val gambar: String? = null,

    @field:SerializedName("url")
    val url: String? = null,
)
