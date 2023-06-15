package com.renufus.agrohealth.ui.main.forum.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.forum.detail.DetailForumResponse
import com.renufus.agrohealth.repositories.ForumRepository
import com.renufus.agrohealth.utility.SingleEventLiveData
import kotlinx.coroutines.launch
import org.koin.dsl.module
import java.lang.Exception

val detailForumViewModelModule = module {
    factory { DetailForumViewModel(get()) }
}
class DetailForumViewModel(private val repository: ForumRepository) : ViewModel() {
    private val gson = Gson()
    val errorStatus by lazy { SingleEventLiveData<Boolean>() }
    val errorMessage by lazy { SingleEventLiveData<String>() }
    val forumContents by lazy { MutableLiveData<DetailForumResponse>() }

    fun getForumContent(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.getForumContentById(id)

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
}
