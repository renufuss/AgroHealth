package com.renufus.agrohealth.ui.main.articles

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.renufus.agrohealth.R
import com.renufus.agrohealth.adapter.ArticleAdapter
import com.renufus.agrohealth.adapter.CategoryAdapter
import com.renufus.agrohealth.data.model.articles.ArticlesItem
import com.renufus.agrohealth.data.model.articles.CategoryItem
import com.renufus.agrohealth.databinding.FragmentArticlesBinding
import com.renufus.agrohealth.ui.main.articles.detail.DetailArticleActivity
import com.renufus.agrohealth.ui.main.articles.detail.DetailArticleFunction
import com.renufus.agrohealth.utility.GeneralUtility
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val articleModule = module {
    factory { ArticlesFragment() }
}

class ArticlesFragment : Fragment(), CategoryAdapter.OnAdapterListener {

    private lateinit var binding: FragmentArticlesBinding
    private val viewModel: ArticlesViewModel by viewModel<ArticlesViewModel>()
    private val utility = GeneralUtility()

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var articleAdapter: ArticleAdapter

    private lateinit var detailArticleFunction: DetailArticleFunction

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryAdapter = createCategoryAdapter()
        articleAdapter = createArticleAdapter()

        binding.recyclerViewArticlesCategory.adapter = categoryAdapter
        binding.recyclerViewArticlesItem.adapter = articleAdapter

        detailArticleFunction = DetailArticleFunction(
            viewModel,
            view,
            viewLifecycleOwner,
            binding,
            utility,
            articleAdapter,
        )

        init()
    }

    private fun init() {
        categoryAdapter.setSelectedCategory(viewModel.categoryId)
        detailArticleFunction.getArticles(viewModel.categoryId)
    }

    override fun onResume() {
        super.onResume()
        categoryAdapter.setSelectedCategory(viewModel.categoryId)
        detailArticleFunction.getArticles(viewModel.categoryId)
    }

    private fun createCategoryAdapter(): CategoryAdapter {
        val categoryId = resources.getStringArray(R.array.category_id)
        val categoryName = resources.getStringArray(R.array.category_name)
        val categories = viewModel.getCategories(categoryId, categoryName)

        return CategoryAdapter(categories, this)
    }

    private fun createArticleAdapter(): ArticleAdapter {
        return ArticleAdapter(
            arrayListOf(),
            object : ArticleAdapter.OnAdapterListener {
                override fun onClick(article: ArticlesItem) {
                    val intent = Intent(requireContext(), DetailArticleActivity::class.java)
                    intent.putExtra(DetailArticleActivity.URL, article.url)
                    startActivity(intent)
                }
            },
        )
    }

    override fun onClick(category: CategoryItem) {
        detailArticleFunction.getArticles(category.id.toInt())
    }
}
