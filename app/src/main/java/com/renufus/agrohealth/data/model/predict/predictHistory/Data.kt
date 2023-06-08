package com.renufus.agrohealth.data.model.predict.predictHistory

import com.google.gson.annotations.SerializedName

data class Data(

	@field:SerializedName("predicted_class")
	val predictedClass: Int,

	@field:SerializedName("message")
	val message: String
)