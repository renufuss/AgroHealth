package com.renufus.agrohealth.data.model.articles

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItem(
    val id: String,
    val name: String,
) : Parcelable
