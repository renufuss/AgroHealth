package com.renufus.agrohealth.ui.main.forum.detail

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.renufus.agrohealth.R

class DetailForumFunction(private val activity: DetailForumActivity) {

    fun getForumContent() {
        activity.viewModel.getForumContent(POST_ID)

        val layoutErrorNetwork = activity.findViewById<ConstraintLayout>(R.id.layout_detailForum_error_network)
        layoutErrorNetwork?.visibility = View.GONE
        activity.binding.nestedScrollDetailForum.visibility = View.VISIBLE

        activity.viewModel.errorStatus.observe(activity) { errorStatus ->
            showLoading(true)
            if (!errorStatus) {
                activity.viewModel.forumContents.observe(activity) { post ->
                    activity.binding.textViewForumDetailItemUsername.text = post.postById?.email
                    activity.binding.textViewForumDetailItemDate.text = post.postById?.createdAt
                    activity.binding.textViewForumDetailItemContent.text = post.postById?.description

                    if (post.postById!!.imageUrl != null) {
                        Glide.with(activity.binding.imageViewForumDetailItemContentImage)
                            .load(post.postById.imageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override(800, 600)
                            .apply(RequestOptions().encodeFormat(Bitmap.CompressFormat.JPEG))
                            .thumbnail(0.25f)
                            .placeholder(R.drawable.text_logo)
                            .error(R.drawable.text_logo)
                            .centerCrop()
                            .into(activity.binding.imageViewForumDetailItemContentImage)

                        activity.binding.imageViewForumDetailItemContentImage.visibility = View.VISIBLE
                    } else {
                        activity.binding.imageViewForumDetailItemContentImage.visibility = View.GONE
                    }
                }
            } else {
                activity.viewModel.errorMessage.observe(activity) { error ->
                    activity.binding.layoutDetailForumErrorNetwork.textViewLayoutErrorNetwork.text = error
                    activity.binding.layoutDetailForumErrorNetwork.buttonLayoutErrorNetwork.setOnClickListener {
                        getForumContent()
                    }
                    activity.binding.nestedScrollDetailForum.visibility = View.GONE
                    layoutErrorNetwork?.visibility = View.VISIBLE
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({
                showLoading(false)
            }, activity.utility.delayLoading)
        }
    }

    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> {
                activity.binding.progressBarDetailForumLoading.visibility = View.VISIBLE
            }

            else -> {
                activity.binding.progressBarDetailForumLoading.visibility = View.INVISIBLE
            }
        }
    }

    companion object {

        var POST_ID = "POST_ID"
    }
}
