package com.renufus.agrohealth.data.model.auth

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(

	@field:SerializedName("refreshToken")
	val refreshToken: String
)