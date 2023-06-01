package com.renufus.agrohealth.data.model.auth

import com.google.gson.annotations.SerializedName

data class ProfileItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)