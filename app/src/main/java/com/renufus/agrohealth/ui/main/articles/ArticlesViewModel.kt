package com.renufus.agrohealth.ui.main.articles

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.renufus.agrohealth.data.model.GeneralResponse
import com.renufus.agrohealth.data.model.articles.ArticlesItem
import com.renufus.agrohealth.data.model.articles.ArticlesResponse
import com.renufus.agrohealth.data.model.articles.CategoryItem
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
    val articles by lazy { MutableLiveData<ArticlesResponse>() }

    val articleItem by lazy { MutableLiveData<List<ArticlesItem>>() }
    var categoryId = 1

    private val isIntro = 1
    private val isBusiness = 2
    private val isFarming = 3
    private val isHistory = 4

    fun getArticles(category: Int) {
        categoryId = category
        viewModelScope.launch {
            try {
                val response = repository.getArticles()
                if (response.isSuccessful) {
                    articles.value = response.body()!!
                    errorStatus.setValue(false)

                    when (category) {
                        isIntro -> {
                            articleItem.value = response.body()!!.data[0].articlesIntro[0].data
                        }
                        isBusiness -> {
                            articleItem.value = response.body()!!.data[0].articlesBusiness[0].data
                        }
                        isFarming -> {
                            articleItem.value = response.body()!!.data[0].articlesFarming[0].data
                        }
                        isHistory -> {
                            articleItem.value = response.body()!!.data[0].articlesHistory[0].data
                        }
                    }
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

    fun getCategories(categoryId: Array<String>, categoryName: Array<String>): ArrayList<CategoryItem> {
        val listCategory = ArrayList<CategoryItem>()
        for (i in categoryId.indices) {
            val category = CategoryItem(categoryId[i], categoryName[i])
            listCategory.add(category)
        }
        return listCategory
    }
}
