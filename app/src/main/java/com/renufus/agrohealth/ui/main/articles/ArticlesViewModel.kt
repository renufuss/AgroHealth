package com.renufus.agrohealth.ui.main.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.articles.ArticlesResponse
import com.renufus.agrohealth.repositories.ArticleRepository
import com.renufus.agrohealth.utility.SingleEventLiveData
import kotlinx.coroutines.launch
import org.koin.dsl.module
import java.lang.Exception

val articleViewModelModule = module {
    factory { ArticlesViewModel(get()) }
}
class ArticlesViewModel(private val repository: ArticleRepository) : ViewModel() {
    private val gson = Gson()
    val errorStatus by lazy { SingleEventLiveData<Boolean>() }
    val errorMessage by lazy { SingleEventLiveData<String>() }
    val articles by lazy { SingleEventLiveData<ArticlesResponse>() }

    fun getArticles() {
        viewModelScope.launch {
            try {
                val response = repository.getArticles()

                if (response.isSuccessful) {
                    articles.setValue(response.body()!!)
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
