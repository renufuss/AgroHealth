package com.renufus.agrohealth.ui.main.articles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.renufus.agrohealth.databinding.FragmentArticlesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val articleModule = module {
    factory { ArticlesFragment() }
}

class ArticlesFragment : Fragment() {

    private lateinit var binding: FragmentArticlesBinding
    private val viewModel: ArticlesViewModel by viewModel<ArticlesViewModel>()

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

        getArticles()
    }

    private fun getArticles() {
        viewModel.getArticles()

        viewModel.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            if (!errorStatus) {
                viewModel.articles.observe(viewLifecycleOwner) { articles ->
                    Log.d("Articless", articles.toString())
                }
            } else {
                viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
