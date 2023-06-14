package com.renufus.agrohealth.ui.main.forum

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.forum.ForumResponse
import com.renufus.agrohealth.data.preferences.UserPreferences
import com.renufus.agrohealth.repositories.ForumRepository
import com.renufus.agrohealth.utility.SingleEventLiveData
import kotlinx.coroutines.launch
import org.koin.dsl.module
import java.lang.Exception

val forumViewModelModule = module {
    factory { ForumViewModel(get(), get()) }
}
class ForumViewModel(private val repository: ForumRepository, val userPreferences: UserPreferences) : ViewModel() {
    private val gson = Gson()
    val errorStatus by lazy { SingleEventLiveData<Boolean>() }
    val errorMessage by lazy { SingleEventLiveData<String>() }
    val forumContents by lazy { MutableLiveData<ForumResponse>() }

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

    fun newPostForum() {
    }
}
