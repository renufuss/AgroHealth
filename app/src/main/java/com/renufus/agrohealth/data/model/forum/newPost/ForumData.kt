package com.renufus.agrohealth.data.model.forum.newPost

import com.google.gson.annotations.SerializedName

data class ForumData(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)