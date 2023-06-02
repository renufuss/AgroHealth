package com.renufus.agrohealth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.renufus.agrohealth.R
import com.renufus.agrohealth.data.model.articles.CategoryItem
import com.renufus.agrohealth.databinding.ItemCategoryBinding

class CategoryAdapter(
    val categories: List<CategoryItem>,
    val listener: OnAdapterListener,
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val items = arrayListOf<TextView>()

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnAdapterListener {
        fun onClick(category: CategoryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.textViewCategoryItemName.text = category.name
        items.add(holder.binding.textViewCategoryItemName)
        holder.itemView.setOnClickListener {
            listener.onClick(category)
            setColor(holder.binding.textViewCategoryItemName)
        }
        setColor(items[0])
    }

    private fun setColor(textView: TextView) {
        items.forEach { it.setBackgroundResource(R.color.white) }
        textView.setBackgroundResource(android.R.color.darker_gray)
    }
}
