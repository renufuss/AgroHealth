package com.renufus.agrohealth.data.model.predictDisease.predictHistory

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.renufus.agrohealth.data.model.predictDisease.PredictData
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictHistoryItem(

    @field:SerializedName("diseaseName")
    override val diseaseName: String? = null,

    @field:SerializedName("createdAt")
    val predictedAt: String? = null,

    @field:SerializedName("historyId")
    val historyId: String? = null,

    @field:SerializedName("imageUrl")
    override val imageUrl: String? = null,

    @field:SerializedName("diseaseSolution")
    override val diseaseSolution: String? = null,

    @field:SerializedName("diseaseDescription")
    override val diseaseDescription: String? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("email")
    val email: String? = null,
) : PredictData, Parcelable
