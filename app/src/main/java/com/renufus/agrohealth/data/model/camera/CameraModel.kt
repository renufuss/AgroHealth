package com.renufus.agrohealth.data.model.camera

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class CameraModel(val image: File) : Parcelable
