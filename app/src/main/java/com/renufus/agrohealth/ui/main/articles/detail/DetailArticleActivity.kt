package com.renufus.agrohealth.ui.main.articles.detail

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.renufus.agrohealth.R
import com.renufus.agrohealth.databinding.ActivityDetailArticleBinding
import com.renufus.agrohealth.utility.GeneralUtility

class DetailArticleActivity : AppCompatActivity() {
    private val binding: ActivityDetailArticleBinding by lazy { ActivityDetailArticleBinding.inflate(layoutInflater) }
    private val utility = GeneralUtility()
    private val url by lazy { intent.getStringExtra(URL) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        utility.setButtonClickAnimation(binding.imageViewDetailArticleButtonBack, R.anim.button_click_animation) {
            finish()
        }

        url?.let {
            val web = binding.webViewDetailArticle
            web.loadUrl(it)
            web.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBarDetailArticleLoading.visibility = View.INVISIBLE
                }
            }

            web.settings.javaScriptCanOpenWindowsAutomatically = false
        }
    }

    companion object {
        const val URL = "url"
    }
}
