package com.renufus.agrohealth.ui.main.forum

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.forum.ForumResponse
import com.renufus.agrohealth.data.model.forum.newPost.NewPostResponse
import com.renufus.agrohealth.data.preferences.UserPreferences
import com.renufus.agrohealth.repositories.ForumRepository
import com.renufus.agrohealth.repositories.UserRepository
import com.renufus.agrohealth.utility.SingleEventLiveData
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.koin.dsl.module
import java.lang.Exception

val forumViewModelModule = module {
    factory { ForumViewModel(get(), get(), get()) }
}
class ForumViewModel(private val repository: ForumRepository, val userPreferences: UserPreferences, private val userRepository: UserRepository) : ViewModel() {
    private val gson = Gson()
    val errorStatus by lazy { SingleEventLiveData<Boolean>() }
    val errorMessage by lazy { SingleEventLiveData<String>() }
    val forumContents by lazy { MutableLiveData<ForumResponse>() }

    val forumPost by lazy { MutableLiveData<NewPostResponse>() }
    val errorTokenStatus by lazy { SingleEventLiveData<Boolean>() }
    val errorForumPostStatus by lazy { SingleEventLiveData<Boolean>() }
    val errorForumPostMessage by lazy { SingleEventLiveData<String>() }

    fun getForumContent() {
        viewModelScope.launch {
            try {
                val response = repository.getForumContent()
                if (response.isSuccessful) {
                    forumContents.value = response.body()!!
                    errorStatus.setValue(false)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = gson.fromJson(errorBody, GeneralResponse::class.java)
                    errorMessage.setValue(errorResponse.message)
                    errorStatus.setValue(true)
                }
            } catch (e: Exception) {
                errorStatus.setValue(true)
                errorMessage.setValue("A network problem occurred")
            }
        }
    }

    fun newPostForum(token: String, description: String, image: MultipartBody.Part?) {
        viewModelScope.launch {
            try {
                val response = repository.newPostForum(token, description, image)

                if (response.isSuccessful) {
                    forumPost.value = response.body()
                    errorForumPostStatus.setValue(false)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = gson.fromJson(errorBody, GeneralResponse::class.java)
                    errorForumPostMessage.setValue(errorResponse.message)
                    errorForumPostStatus.setValue(true)
                }
            } catch (e: Exception) {
                errorForumPostStatus.setValue(true)
                errorForumPostMessage.setValue("A network problem occurred")
            }
        }
    }

    fun refreshToken() {
        viewModelScope.launch {
            try {
                val response = userRepository.refreshToken(userPreferences.getToken()!!)

                if (response.isSuccessful) {
                    userPreferences.setToken(response.body()!!.refreshToken)
                    errorTokenStatus.setValue(false)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = gson.fromJson(errorBody, GeneralResponse::class.java)
                    errorForumPostMessage.setValue(errorResponse.message)
                    errorTokenStatus.setValue(true)
                }
            } catch (e: Exception) {
                errorForumPostStatus.setValue(true)
                errorTokenStatus.setValue(true)
                errorForumPostMessage.setValue("Your token is expired, Make sure you only log in on 1 device")
            }
        }
    }
}
