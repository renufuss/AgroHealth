package com.renufus.agrohealth.data.model.forum.newPost

import com.google.gson.annotations.SerializedName

data class NewPostResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("forumData")
	val forumData: ForumData? = null
)