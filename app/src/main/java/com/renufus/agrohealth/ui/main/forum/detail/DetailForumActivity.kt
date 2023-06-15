package com.renufus.agrohealth.ui.main.forum.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.renufus.agrohealth.R
import com.renufus.agrohealth.databinding.ActivityDetailForumBinding
import com.renufus.agrohealth.utility.GeneralUtility
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val detailForumModule = module {
    factory { DetailForumActivity() }
}
class DetailForumActivity : AppCompatActivity() {
    val binding: ActivityDetailForumBinding by lazy { ActivityDetailForumBinding.inflate(layoutInflater) }
    val utility = GeneralUtility()

    val viewModel: DetailForumViewModel by viewModel<DetailForumViewModel>()
    private val detailForumFunction = DetailForumFunction(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        utility.setButtonClickAnimation(binding.imageViewDetailForumButtonBack, R.anim.button_click_animation) {
            finish()
        }

        detailForumFunction.getForumContent()
    }
}
