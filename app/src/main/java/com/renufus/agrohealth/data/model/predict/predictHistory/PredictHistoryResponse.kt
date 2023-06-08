package com.renufus.agrohealth.data.model.predict.predictHistory

import com.google.gson.annotations.SerializedName

data class PredictHistoryResponse(

    @field:SerializedName("totalResult")
    val totalResult: Int,

    @field:SerializedName("data")
    val data: List<HistoryItem>,

    @field:SerializedName("status")
    val status: Int,
)
