package com.renufus.agrohealth.data.model.predict

import com.google.gson.annotations.SerializedName
import com.renufus.agrohealth.data.model.predict.predictHistory.Data

data class PredictResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("status")
    val status: Int? = null,
)
