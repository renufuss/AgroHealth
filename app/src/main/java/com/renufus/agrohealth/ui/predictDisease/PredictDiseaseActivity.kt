package com.renufus.agrohealth.ui.predictDisease

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.renufus.agrohealth.R
import com.renufus.agrohealth.data.model.camera.CameraModel
import com.renufus.agrohealth.data.model.camera.MyImage
import com.renufus.agrohealth.databinding.ActivityPredictDiseaseBinding
import com.renufus.agrohealth.utility.Constants
import com.renufus.agrohealth.utility.GeneralUtility
import com.renufus.agrohealth.utility.rotateBitmap
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class PredictDiseaseActivity : AppCompatActivity() {

    private val binding: ActivityPredictDiseaseBinding by lazy { ActivityPredictDiseaseBinding.inflate(layoutInflater) }
    private val viewModel: PredictDiseaseViewModel by viewModel<PredictDiseaseViewModel>()
    private val utility = GeneralUtility()
    private var imagePredict: MyImage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        renderImage()
        utility.setStatusBarColor(this@PredictDiseaseActivity, Color.WHITE)

        utility.setButtonClickAnimation(binding.imageViewProcessCameraButtonBack, R.anim.button_click_animation) {
            finish()
        }

        processImage()
    }

    private fun renderImage() {
        val scanImageCameraX = intent.getParcelableExtra<CameraModel>(CAMERA_X_RESULT)
        val scanImageGallery = intent.getParcelableExtra<CameraModel>(GALLERY_RESULT)

        if (scanImageCameraX != null) {
            imagePredict = MyImage(scanImageCameraX.image, Constants.CAMERA)
            val image = BitmapFactory.decodeFile(scanImageCameraX.image.path)
            val fixImage = rotateBitmap(image, IS_BACK_CAMERA)
            binding.imageViewProcessCameraPreview.setImageBitmap(fixImage)
        }

        if (scanImageGallery != null) {
            imagePredict = MyImage(scanImageGallery.image, Constants.GALLERY)
            val image = BitmapFactory.decodeFile(scanImageGallery.image.path)
            binding.imageViewProcessCameraPreview.setImageBitmap(image)
        }
    }

    private fun processImage() {
        if (imagePredict != null) {
            val file = reduceFileImage(imagePredict!!.image)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile,
            )
            viewModel.predictDisease(imageMultipart)

            viewModel.errorStatus.observe(this) { errorStatus ->
                if (!errorStatus) {
                    viewModel.prediction.observe(this) { prediction ->
                        Log.d("predictionIs", prediction.toString())
                    }
                } else {
                    viewModel.errorMessage.observe(this) { error ->
                        Log.d("predictionIs", error.toString())
                    }
                }
            }
        } else {
            Log.d("predictionIs", "noImage")
        }
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    companion object {
        const val CAMERA_X_RESULT = "cameraXResult"
        const val GALLERY_RESULT = "galleryResult"
        var IS_BACK_CAMERA = false
        private const val MAXIMAL_SIZE = 1000000
    }
}