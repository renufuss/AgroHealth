package com.renufus.agrohealth.repositories

import com.renufus.agrohealth.data.api.ApiService
import com.renufus.agrohealth.data.model.articles.ArticlesResponse
import org.koin.dsl.module
import retrofit2.Response

val articleRepositoryModule = module {
    factory { ArticleRepository(get()) }
}
class ArticleRepository(private val apiService: ApiService) {

    suspend fun getArticles(): Response<ArticlesResponse> {
        return apiService.getArticles()
    }
}
