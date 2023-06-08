package com.renufus.agrohealth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.renufus.agrohealth.R
import com.renufus.agrohealth.data.model.predict.predictHistory.HistoryItem
import com.renufus.agrohealth.databinding.ItemHistoryBinding
import com.renufus.agrohealth.utility.GeneralUtility

class PredictHistoryAdapter(
    val histories: ArrayList<HistoryItem>,
    val listener: OnAdapterListener,
) : RecyclerView.Adapter<PredictHistoryAdapter.ViewHolder>() {

    val utility = GeneralUtility()

    interface OnAdapterListener {
        fun onClick(history: HistoryItem)
    }

    interface OnImageClickListener {
        fun onImageClick(imageUrl: String)
    }

    class ViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun getItemCount() = histories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = histories[position]
        holder.binding.textViewHistoryItemDiseaseName.text = history.diseaseName
        holder.binding.textViewHistoryItemLatinDiseaseName.text = history.diseaseLatin
        holder.binding.textViewHistoryItemPredictDate.text = history.predictedAt
        Glide.with(holder.binding.imageViewHistoryItemPreview)
            .load(history.diseaseImage)
            .placeholder(R.drawable.text_logo)
            .error(R.drawable.text_logo)
            .centerCrop()
            .into(holder.binding.imageViewHistoryItemPreview)

        holder.itemView.setOnClickListener {
            listener.onClick(history)
        }
    }

    fun add(data: List<HistoryItem>) {
        histories.clear()
        histories.addAll(data)
        notifyDataSetChanged()
    }
}
