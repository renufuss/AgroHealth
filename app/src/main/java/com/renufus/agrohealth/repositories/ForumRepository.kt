package com.renufus.agrohealth.repositories

import com.renufus.agrohealth.data.api.ApiService
import com.renufus.agrohealth.data.model.forum.ForumResponse
import com.renufus.agrohealth.data.model.forum.detail.DetailForumResponse
import com.renufus.agrohealth.data.model.forum.newPost.NewPostResponse
import okhttp3.MultipartBody
import org.koin.dsl.module
import retrofit2.Response

val forumRepositoryModule = module {
    factory { ForumRepository(get()) }
}

class ForumRepository(private val apiService: ApiService) {

    suspend fun getForumContent(): Response<ForumResponse> {
        return apiService.getForumContent()
    }

    suspend fun getForumContentById(id: String): Response<DetailForumResponse> {
        return apiService.getForumContentById(id)
    }

    suspend fun newPostForum(token: String, email: String, description: String, image: MultipartBody.Part): Response<NewPostResponse> {
        return apiService.newPostForum(token, email, description, image)
    }
}
