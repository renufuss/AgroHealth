package com.renufus.agrohealth.repositories

import com.renufus.agrohealth.data.api.ApiService
import com.renufus.agrohealth.data.model.forum.ForumResponse
import org.koin.dsl.module
import retrofit2.Response

val forumRepositoryModule = module {
    factory { ForumRepository(get()) }
}

class ForumRepository(private val apiService: ApiService) {

    suspend fun getForumContent(): Response<ForumResponse> {
        return apiService.getForumContent()
    }
}
