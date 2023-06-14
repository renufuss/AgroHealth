package com.renufus.agrohealth.data.model.predictDisease.predictHistory

import com.google.gson.annotations.SerializedName

data class PredictHistoryResponse(

    @field:SerializedName("data")
    val data: List<PredictHistoryItem>,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null,
)
