package com.renufus.agrohealth.data.model.articles

import com.google.gson.annotations.SerializedName

data class ArticlesCategory(

    @field:SerializedName("articles_farming")
    val articlesFarming: List<ArticlesFarmingItem>,

    @field:SerializedName("articles_intro")
    val articlesIntro: List<ArticlesIntroItem>,

    @field:SerializedName("articles_business")
    val articlesBusiness: List<ArticlesBusinessItem>,

    @field:SerializedName("articles_history")
    val articlesHistory: List<ArticlesHistoryItem>,

)
