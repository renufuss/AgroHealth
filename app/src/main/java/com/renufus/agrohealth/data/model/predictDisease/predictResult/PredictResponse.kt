package com.renufus.agrohealth.data.model.predictDisease.predictResult

import com.google.gson.annotations.SerializedName

data class PredictResponse(

    @field:SerializedName("data")
    val data: PredictItem? = null,

    @field:SerializedName("status")
    val status: Int? = null,
)
