package com.renufus.agrohealth.ui.main.articles.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.renufus.agrohealth.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }

    companion object {
        const val URL = "url"
    }
}
