package com.renufus.agrohealth.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.renufus.agrohealth.R
import com.renufus.agrohealth.data.model.articles.ArticlesItem
import com.renufus.agrohealth.databinding.ItemArticleBinding
import com.renufus.agrohealth.databinding.ItemHeadlineBinding

private const val HEADLINE = 1
private const val BASIC = 2

class ArticleAdapter(
    val articles: ArrayList<ArticlesItem>,
    val listener: OnAdapterListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        var VIEW_TYPES = HEADLINE
    }

    class ViewHolderHeadline(val binding: ItemHeadlineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem) {
            binding.textViewHeadlineItemTitle.text = article.title
            Glide.with(binding.imageViewHeadlineItemThumbnail)
                .load(article.gambar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(800, 600)
                .apply(RequestOptions().encodeFormat(Bitmap.CompressFormat.JPEG))
                .thumbnail(0.25f)
                .placeholder(R.drawable.text_logo)
                .error(R.drawable.text_logo)
                .centerCrop()
                .into(binding.imageViewHeadlineItemThumbnail)
        }
    }

    class ViewHolderBasic(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem) {
            binding.textViewArticleItemTitle.text = article.title
            Glide.with(binding.imageViewArticleItemThumbnail)
                .load(article.gambar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(800, 600)
                .apply(RequestOptions().encodeFormat(Bitmap.CompressFormat.JPEG))
                .thumbnail(0.25f)
                .placeholder(R.drawable.text_logo)
                .error(R.drawable.text_logo)
                .centerCrop()
                .into(binding.imageViewArticleItemThumbnail)
        }
    }

    interface OnAdapterListener {
        fun onClick(article: ArticlesItem)
    }

    override fun getItemViewType(position: Int) = VIEW_TYPES

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEADLINE) {
            ViewHolderHeadline(
                ItemHeadlineBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            )
        } else {
            ViewHolderBasic(
                ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            )
        }
    }

    override fun getItemCount() = articles.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val article = articles[position]
        if (VIEW_TYPES == HEADLINE) {
            (holder as ViewHolderHeadline).bind(article)
        } else if (VIEW_TYPES == BASIC) {
            (holder as ViewHolderBasic).bind(article)
        }
        holder.itemView.setOnClickListener {
            listener.onClick(article)
        }
    }

    fun add(data: List<ArticlesItem>) {
        articles.clear()
        articles.addAll(data)
        notifyDataSetChanged()
    }
}
