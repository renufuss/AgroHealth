package com.renufus.agrohealth.ui.main.forum.detail

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.renufus.agrohealth.R
import com.renufus.agrohealth.databinding.ActivityDetailForumBinding
import com.renufus.agrohealth.utility.GeneralUtility
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val detailForumModule = module {
    factory { DetailForumActivity() }
}
class DetailForumActivity : AppCompatActivity() {
    private val binding: ActivityDetailForumBinding by lazy { ActivityDetailForumBinding.inflate(layoutInflater) }
    private val utility = GeneralUtility()

    private val viewModel: DetailForumViewModel by viewModel<DetailForumViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        utility.setButtonClickAnimation(binding.imageViewDetailForumButtonBack, R.anim.button_click_animation) {
            finish()
        }

        getForumContent()
    }

    private fun getForumContent() {
        viewModel.getForumContent(POST_ID)

        val layoutErrorNetwork = findViewById<ConstraintLayout>(R.id.layout_detailForum_error_network)
        layoutErrorNetwork?.visibility = View.GONE
        binding.nestedScrollDetailForum.visibility = View.VISIBLE

        viewModel.errorStatus.observe(this) { errorStatus ->
            showLoading(true)
            if (!errorStatus) {
                viewModel.forumContents.observe(this) { post ->
                    binding.textViewForumDetailItemUsername.text = post.postById?.email
                    binding.textViewForumDetailItemDate.text = post.postById?.createdAt
                    binding.textViewForumDetailItemContent.text = post.postById?.description

                    if (post.postById!!.imageUrl != null) {
                        Glide.with(binding.imageViewForumDetailItemContentImage)
                            .load(post.postById.imageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override(800, 600)
                            .apply(RequestOptions().encodeFormat(Bitmap.CompressFormat.JPEG))
                            .thumbnail(0.25f)
                            .placeholder(R.drawable.text_logo)
                            .error(R.drawable.text_logo)
                            .centerCrop()
                            .into(binding.imageViewForumDetailItemContentImage)

                        binding.imageViewForumDetailItemContentImage.visibility = View.VISIBLE
                    } else {
                        binding.imageViewForumDetailItemContentImage.visibility = View.GONE
                    }
                }
            } else {
                viewModel.errorMessage.observe(this) { error ->
                    binding.layoutDetailForumErrorNetwork.textViewLayoutErrorNetwork.text = error
                    binding.layoutDetailForumErrorNetwork.buttonLayoutErrorNetwork.setOnClickListener {
                        getForumContent()
                    }
                    binding.nestedScrollDetailForum.visibility = View.GONE
                    layoutErrorNetwork?.visibility = View.VISIBLE
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({
                showLoading(false)
            }, utility.delayLoading)
        }
    }

    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> {
                binding.progressBarDetailForumLoading.visibility = View.VISIBLE
            }

            else -> {
                binding.progressBarDetailForumLoading.visibility = View.INVISIBLE
            }
        }
    }

    companion object {
        var POST_ID = "POST_ID"
    }
}
