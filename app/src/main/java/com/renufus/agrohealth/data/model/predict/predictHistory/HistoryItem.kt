package com.renufus.agrohealth.data.model.predict.predictHistory

import com.google.gson.annotations.SerializedName

data class HistoryItem(

    @field:SerializedName("diseaseName")
    val diseaseName: String,

    @field:SerializedName("diseaseImage")
    val diseaseImage: String,

    @field:SerializedName("predictedAt")
    val predictedAt: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("diseaseLatin")
    val diseaseLatin: String,
)
