package com.renufus.agrohealth.ui.main.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.renufus.agrohealth.R
import com.renufus.agrohealth.adapter.ArticleAdapter
import com.renufus.agrohealth.adapter.CategoryAdapter
import com.renufus.agrohealth.data.model.articles.ArticlesItem
import com.renufus.agrohealth.data.model.articles.CategoryItem
import com.renufus.agrohealth.databinding.FragmentArticlesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val articleModule = module {
    factory { ArticlesFragment() }
}

class ArticlesFragment : Fragment() {

    private lateinit var binding: FragmentArticlesBinding
    private val viewModel: ArticlesViewModel by viewModel<ArticlesViewModel>()

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

        viewModel.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            if (!errorStatus) {
                viewModel.articleItem.observe(viewLifecycleOwner) { articles ->

                    binding.imageViewArticleAlert.visibility = if (articles.isEmpty()) View.VISIBLE else View.GONE
                    binding.textViewArticleAlert.visibility = if (articles.isEmpty()) View.VISIBLE else View.GONE
                    articleAdapter.add(articles)
                }
            } else {
                viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val articleAdapter by lazy {
        ArticleAdapter(
            arrayListOf(),
            object : ArticleAdapter.OnAdapterListener {
                override fun onClick(article: ArticlesItem) {
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
