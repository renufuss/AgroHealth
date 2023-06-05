package com.renufus.agrohealth.ui.main.articles

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.content.Intent
import com.renufus.agrohealth.R
import com.renufus.agrohealth.adapter.ArticleAdapter
import com.renufus.agrohealth.adapter.CategoryAdapter
import com.renufus.agrohealth.data.model.articles.ArticlesItem
import com.renufus.agrohealth.data.model.articles.CategoryItem
import com.renufus.agrohealth.databinding.FragmentArticlesBinding
import com.renufus.agrohealth.ui.main.articles.detail.DetailArticleActivity
import com.renufus.agrohealth.utility.GeneralUtility
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val articleModule = module {
    factory { ArticlesFragment() }
}

class ArticlesFragment : Fragment() {

    private lateinit var binding: FragmentArticlesBinding
    private val viewModel: ArticlesViewModel by viewModel<ArticlesViewModel>()
    private val utility = GeneralUtility()

    private lateinit var article: List<ArticlesItem>

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
        binding.recyclerViewArticlesCategory.adapter = categoryAdapter
        binding.recyclerViewArticlesItem.adapter = articleAdapter

        getArticles(1)
    }

    private fun getArticles(category: Int) {
        viewModel.getArticles(category)

        val isIntro = 1
        val isHeadline = 1
        val isBasic = 2
        if(category == isIntro){
            ArticleAdapter.VIEW_TYPES = isHeadline
        }else{
            ArticleAdapter.VIEW_TYPES = isBasic
        }

        viewModel.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            showLoading(true)
            if (!errorStatus) {
                viewModel.articleItem.observe(viewLifecycleOwner) { articles ->

                    binding.imageViewArticleAlert.visibility = if (articles.isEmpty()) View.VISIBLE else View.GONE
                    binding.textViewArticleAlert.visibility = if (articles.isEmpty()) View.VISIBLE else View.GONE
                    binding.recyclerViewArticlesItem.visibility = if (articles.isEmpty()) View.GONE else View.VISIBLE
                    articleAdapter.add(articles)
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    showLoading(false)
                    binding.nestedScrollArticle.scrollTo(0, 0)
                }, utility.delayLoading)
            } else {
                viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> {
                binding.progressBarArticleTop.visibility = View.VISIBLE
                binding.recyclerViewArticlesItem.visibility = View.GONE
            }

            else -> {
                binding.progressBarArticleTop.visibility = View.INVISIBLE
                binding.recyclerViewArticlesItem.visibility = View.VISIBLE
            }
        }
    }

    private val articleAdapter by lazy {
        ArticleAdapter(
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

    private val categoryAdapter by lazy {
        val categoryId = resources.getStringArray(R.array.category_id)
        val categoryName = resources.getStringArray(R.array.category_name)
        val categories = viewModel.getCategories(categoryId, categoryName)
        CategoryAdapter(
            categories,
            object : CategoryAdapter.OnAdapterListener {
                override fun onClick(category: CategoryItem) {
                    getArticles(category.id.toInt())
                }
            },
        )
    }
}
