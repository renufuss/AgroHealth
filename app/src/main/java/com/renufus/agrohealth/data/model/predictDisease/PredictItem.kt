package com.renufus.agrohealth.data.model.predictDisease

import com.google.gson.annotations.SerializedName

data class PredictItem(

    @field:SerializedName("diseaseName")
    val diseaseName: String? = null,

    @field:SerializedName("predictedAt")
    val predictedAt: String? = null,

    @field:SerializedName("predicted_class")
    val predictedClass: Int? = null,

    @field:SerializedName("diseaseSolution")
    val diseaseSolution: String? = null,

    @field:SerializedName("diseaseDescription")
    val diseaseDescription: String? = null,

    @field:SerializedName("label")
    val label: String? = null,

    @field:SerializedName("message")
    val message: String? = null,
)
