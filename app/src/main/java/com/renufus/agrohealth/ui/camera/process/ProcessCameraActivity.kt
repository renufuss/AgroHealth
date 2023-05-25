package com.renufus.agrohealth.ui.camera.process

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.renufus.agrohealth.data.model.CameraModel
import com.renufus.agrohealth.databinding.ActivityProcessCameraBinding
import com.renufus.agrohealth.utility.GeneralUtility
import com.renufus.agrohealth.utility.rotateBitmap

class ProcessCameraActivity : AppCompatActivity() {

    private val binding: ActivityProcessCameraBinding by lazy { ActivityProcessCameraBinding.inflate(layoutInflater) }
    private val utility = GeneralUtility()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        renderImage()
        utility.setStatusBarColor(this@ProcessCameraActivity, Color.TRANSPARENT)
    }

    private fun renderImage() {
        val scanImageCameraX = intent.getParcelableExtra<CameraModel>(CAMERA_X_RESULT)
        val scanImageGallery = intent.getParcelableExtra<CameraModel>(GALLERY_RESULT)

        if (scanImageCameraX != null) {
            val image = BitmapFactory.decodeFile(scanImageCameraX.image.path)
            val fixImage = rotateBitmap(image, true)
            binding.imageViewProcessCameraPreview.setImageBitmap(fixImage)
        }

        if (scanImageGallery != null) {
            val image = BitmapFactory.decodeFile(scanImageGallery.image.path)
            binding.imageViewProcessCameraPreview.setImageBitmap(image)
        }
    }

    companion object {
        const val CAMERA_X_RESULT = "cameraXResult"
        const val GALLERY_RESULT = "galleryResult"
    }
}
