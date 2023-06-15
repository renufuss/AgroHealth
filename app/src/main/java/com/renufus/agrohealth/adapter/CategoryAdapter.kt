package com.renufus.agrohealth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renufus.agrohealth.R
import com.renufus.agrohealth.data.model.articles.CategoryItem
import com.renufus.agrohealth.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val categories: List<CategoryItem>,
    private val listener: OnAdapterListener,
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var selectedCategoryIndex: Int = -1

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnAdapterListener {
        fun onClick(category: CategoryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        val textViewCategoryItemName = holder.binding.textViewCategoryItemName
        textViewCategoryItemName.text = category.name

        val categoryIndex = position + 1 // Adjust the index to start from 1

        if (categoryIndex == selectedCategoryIndex) {
            textViewCategoryItemName.setBackgroundResource(android.R.color.darker_gray)
        } else {
            textViewCategoryItemName.setBackgroundResource(R.color.white)
        }

        textViewCategoryItemName.setOnClickListener {
            listener.onClick(category)
            setSelectedCategory(categoryIndex)
        }
    }

    fun setSelectedCategory(index: Int) {
        selectedCategoryIndex = index
        notifyDataSetChanged()
    }
}
