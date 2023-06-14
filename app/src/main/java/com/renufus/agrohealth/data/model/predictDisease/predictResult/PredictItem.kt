package com.renufus.agrohealth.data.model.predictDisease.predictResult

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.renufus.agrohealth.data.model.predictDisease.PredictData
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictItem(

    @field:SerializedName("diseaseName")
    override val diseaseName: String? = null,

    @field:SerializedName("predictedAt")
    val predictedAt: String? = null,

    @field:SerializedName("predicted_class")
    val predictedClass: Int? = null,

    @field:SerializedName("imageUrl")
    override val imageUrl: String? = null,

    @field:SerializedName("diseaseSolution")
    override val diseaseSolution: String? = null,

    @field:SerializedName("diseaseDescription")
    override val diseaseDescription: String? = null,

    @field:SerializedName("label")
    val label: String? = null,

    @field:SerializedName("message")
    val message: String? = null,
) : PredictData, Parcelable
