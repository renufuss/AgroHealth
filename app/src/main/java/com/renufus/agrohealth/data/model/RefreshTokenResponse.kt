package com.renufus.agrohealth.data.model

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(

	@field:SerializedName("refreshToken")
	val refreshToken: String
)