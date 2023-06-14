package com.renufus.agrohealth.ui.predictDisease.process

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import com.renufus.agrohealth.R
import com.renufus.agrohealth.data.model.camera.CameraModel
import com.renufus.agrohealth.data.model.camera.MyImage
import com.renufus.agrohealth.databinding.ActivityPredictDiseaseProcessBinding
import com.renufus.agrohealth.ui.auth.login.LoginActivity
import com.renufus.agrohealth.ui.camera.CameraActivity
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

class PredictDiseaseProcessActivity : AppCompatActivity() {

    private val binding: ActivityPredictDiseaseProcessBinding by lazy { ActivityPredictDiseaseProcessBinding.inflate(layoutInflater) }
    private val viewModel: PredictDiseaseViewModel by viewModel<PredictDiseaseViewModel>()
    private val utility = GeneralUtility()
    private var imagePredict: MyImage? = null
    private var processDelay = 1200L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        utility.setStatusBarColor(this@PredictDiseaseProcessActivity, Color.WHITE)

        utility.setButtonClickAnimation(binding.imageViewProcessCameraButtonBack, R.anim.button_click_animation) {
            finish()
        }

        utility.setButtonClickAnimation(binding.buttonProcessCameraTryAgain, R.anim.button_click_animation) {
            utility.moveToAnotherActivity(this@PredictDiseaseProcessActivity, CameraActivity::class.java)
            finish()
        }
        renderImage()
    }

    override fun onStart() {
        super.onStart()

        Handler(Looper.getMainLooper()).postDelayed({
            processImage()
        }, processDelay)
    }

    private fun renderImage() {
        val scanImageCameraX = intent.getParcelableExtra<CameraModel>(CAMERA_X_RESULT)
        val scanImageGallery = intent.getParcelableExtra<CameraModel>(GALLERY_RESULT)

        if (scanImageCameraX != null) {
            imagePredict = MyImage(scanImageCameraX.image, Constants.CAMERA)
            val correctedFile = reduceFileImage(scanImageCameraX.image)
            val image = BitmapFactory.decodeFile(correctedFile.path)
            binding.imageViewProcessCameraPreview.setImageBitmap(image)
        }

        if (scanImageGallery != null) {
            imagePredict = MyImage(scanImageGallery.image, Constants.GALLERY)
            val correctedFile = reduceFileImage(scanImageGallery.image)
            val image = BitmapFactory.decodeFile(correctedFile.path)
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
            viewModel.refreshToken()
            viewModel.errorTokenStatus.observe(this) { errorTokenStatus ->
                showLoading(true)
                if (!errorTokenStatus) {
                    viewModel.predictDisease(imageMultipart)
                    viewModel.errorStatus.observe(this) { errorStatus ->
                        showLoading(true)
                        if (!errorStatus) {
                            viewModel.prediction.observe(this) { prediction ->
                                showLoading(false)
                                Log.d("predictionIs", prediction.toString())
                            }
                        } else {
                            viewModel.errorMessage.observe(this) { error ->
                                showLoading(false)
                                binding.textViewProcessCameraDescription.text = error
                                binding.buttonProcessCameraTryAgain.visibility = View.VISIBLE
                            }
                        }
                    }
                } else {
                    viewModel.errorMessage.observe(this) { error ->
                        showLoading(false)
                        binding.textViewProcessCameraDescription.text = error
                        binding.buttonProcessCameraTryAgain.text = "Login"
                        binding.buttonProcessCameraTryAgain.setOnClickListener {
                            logout()
                        }
                        binding.buttonProcessCameraTryAgain.visibility = View.VISIBLE
                    }
                }
            }
        } else {
            Toast.makeText(this@PredictDiseaseProcessActivity, "You haven't added a picture yet", Toast.LENGTH_SHORT).show()
        }
    }

    fun reduceFileImage(file: File): File {
        val exif = ExifInterface(file.path)
        val rotation = when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

        val bitmap = BitmapFactory.decodeFile(file.path)
        val rotatedBitmap = rotateBitmap(bitmap, rotation)

        val tempFile = File.createTempFile("rotated_", ".jpg", cacheDir)
        tempFile.deleteOnExit()

        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(tempFile))

        return tempFile
    }

    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> {
                binding.progressBarProcessCameraLoading.visibility = View.VISIBLE
                binding.buttonProcessCameraTryAgain.visibility = View.GONE
            }

            else -> {
                binding.progressBarProcessCameraLoading.visibility = View.GONE
            }
        }
    }

    private fun logout() {
        viewModel.userPreferences.setStatusLogin(false)
        viewModel.userPreferences.setToken("tokenApi")

        utility.moveToAnotherActivity(this@PredictDiseaseProcessActivity, LoginActivity::class.java)
    }
    companion object {
        const val CAMERA_X_RESULT = "cameraXResult"
        const val GALLERY_RESULT = "galleryResult"
        var IS_BACK_CAMERA = false
        private const val MAXIMAL_SIZE = 1000000
    }
}
