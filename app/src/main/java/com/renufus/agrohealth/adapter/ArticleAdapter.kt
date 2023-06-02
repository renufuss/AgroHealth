package com.renufus.agrohealth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.renufus.agrohealth.R
import com.renufus.agrohealth.data.model.articles.ArticlesItem
import com.renufus.agrohealth.databinding.ItemArticleBinding

class ArticleAdapter(
    val articles: ArrayList<ArticlesItem>,
    val listener: OnAdapterListener,
) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnAdapterListener {
        fun onClick(article: ArticlesItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun getItemCount() = articles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.binding.textViewArticleItemTitle.text = article.title
        Glide.with(holder.binding.imageViewArticleItemThumbnail)
            .load(article.gambar)
            .placeholder(R.drawable.text_logo)
            .error(R.drawable.text_logo)
            .centerCrop()
            .into(holder.binding.imageViewArticleItemThumbnail)

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
