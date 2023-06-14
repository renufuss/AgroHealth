package com.renufus.agrohealth.data.model.predictDisease.predictHistory

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictHistoryItem(

    @field:SerializedName("diseaseName")
    val diseaseName: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("historyId")
    val historyId: String? = null,

    @field:SerializedName("imageUrl")
    val imageUrl: String? = null,

    @field:SerializedName("diseaseSolution")
    val diseaseSolution: String? = null,

    @field:SerializedName("diseaseDescription")
    val diseaseDescription: String? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("email")
    val email: String? = null,
) : Parcelable
