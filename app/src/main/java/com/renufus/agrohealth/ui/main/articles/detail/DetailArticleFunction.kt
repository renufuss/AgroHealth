package com.renufus.agrohealth.ui.main.articles.detail

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import com.renufus.agrohealth.R
import com.renufus.agrohealth.adapter.ArticleAdapter
import com.renufus.agrohealth.databinding.FragmentArticlesBinding
import com.renufus.agrohealth.ui.main.articles.ArticlesViewModel
import com.renufus.agrohealth.utility.GeneralUtility

class DetailArticleFunction(
    private val viewModel: ArticlesViewModel,
    private val view: View?,
    private val viewLifecycleOwner: LifecycleOwner,
    private val binding: FragmentArticlesBinding,
    private val utility: GeneralUtility,
    private val articleAdapter: ArticleAdapter,
) {
    fun getArticles(category: Int) {
        viewModel.getArticles(category)

        val layoutErrorNetwork = view?.findViewById<ConstraintLayout>(R.id.layout_article_error_network)
        layoutErrorNetwork?.visibility = View.GONE
        binding.nestedScrollArticle.visibility = View.VISIBLE

        val isIntro = 1
        val isHeadline = 1
        val isBasic = 2
        if (category == isIntro) {
            ArticleAdapter.VIEW_TYPES = isHeadline
        } else {
            ArticleAdapter.VIEW_TYPES = isBasic
        }

        viewModel.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            showLoading(true)
            if (!errorStatus) {
                viewModel.articleItem.observe(viewLifecycleOwner) { articles ->
                    binding.imageViewArticleAlert.visibility =
                        if (articles.isEmpty()) View.VISIBLE else View.GONE
                    binding.textViewArticleAlert.visibility =
                        if (articles.isEmpty()) View.VISIBLE else View.GONE
                    binding.recyclerViewArticlesItem.visibility =
                        if (articles.isEmpty()) View.GONE else View.VISIBLE
                    articleAdapter.add(articles)
                }
            } else {
                viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                    binding.imageViewArticleAlert.visibility = View.GONE
                    binding.textViewArticleAlert.visibility = View.GONE
                    binding.nestedScrollArticle.visibility = View.GONE
                    binding.layoutArticleErrorNetwork.textViewLayoutErrorNetwork.text = error
                    binding.layoutArticleErrorNetwork.buttonLayoutErrorNetwork.setOnClickListener {
                        getArticles(viewModel.categoryId)
                    }

                    layoutErrorNetwork?.visibility = View.VISIBLE
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({
                showLoading(false)
                binding.nestedScrollArticle.scrollTo(0, 0)
            }, utility.delayLoading)
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
}
