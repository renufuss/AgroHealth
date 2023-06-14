package com.renufus.agrohealth.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.renufus.agrohealth.R
import com.renufus.agrohealth.data.model.forum.ForumItem
import com.renufus.agrohealth.databinding.ItemForumBinding
import com.renufus.agrohealth.utility.GeneralUtility

class ForumAdapter(
    val forums: ArrayList<ForumItem>,
    val listener: OnAdapterListener,
    val imageClickListener: OnImageClickListener,
) : RecyclerView.Adapter<ForumAdapter.ViewHolder>() {

    val utility = GeneralUtility()

    interface OnAdapterListener {
        fun onClick(forum: ForumItem)
    }

    interface OnImageClickListener {
        fun onImageClick(imageUrl: String)
    }

    class ViewHolder(val binding: ItemForumBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemForumBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun getItemCount() = forums.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forum = forums[position]
        holder.binding.textViewForumItemUsername.text = forum.email
        holder.binding.textViewForumItemDate.text = forum.createdAt
        holder.binding.textViewForumItemContent.text = forum.description
//        holder.binding.textViewForumItemCommentCounter.text = utility.prettyCount(forum.commentCounter!!)
//        holder.binding.textViewForumItemLikeCounter.text = utility.prettyCount(forum.likeCounter!!)
        if (forum.imageUrl != null) {
            Glide.with(holder.binding.imageViewForumItemContentImage)
                .load(forum.imageUrl)
                .placeholder(R.drawable.text_logo)
                .error(R.drawable.text_logo)
                .centerCrop()
                .into(holder.binding.imageViewForumItemContentImage)

            holder.binding.imageViewForumItemContentImage.setOnClickListener {
                imageClickListener.onImageClick(forum.imageUrl)
            }
        } else {
            holder.binding.imageViewForumItemContentImage.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            listener.onClick(forum)
        }
    }

    fun add(data: List<ForumItem>) {
        forums.clear()
        forums.addAll(data)
        notifyDataSetChanged()
    }
}
