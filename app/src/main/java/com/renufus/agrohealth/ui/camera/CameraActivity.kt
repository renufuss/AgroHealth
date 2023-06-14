package com.renufus.agrohealth.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.renufus.agrohealth.R
import com.renufus.agrohealth.data.model.camera.CameraModel
import com.renufus.agrohealth.databinding.ActivityCameraBinding
import com.renufus.agrohealth.ui.predictDisease.process.PredictDiseaseProcessActivity
import com.renufus.agrohealth.utility.GeneralUtility
import com.renufus.agrohealth.utility.createFile
import com.renufus.agrohealth.utility.uriToFile
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    private val binding: ActivityCameraBinding by lazy { ActivityCameraBinding.inflate(layoutInflater) }
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var cameraExecutor: ExecutorService
    private val utility = GeneralUtility()
    private var isImageCaptureInProgress = false

    private var getFile: File? = null
    private val launcherIntentGalley =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val selectedImage = it.data?.data as Uri
                val myFile = uriToFile(selectedImage, this)
                getFile = myFile

                startProcessCamera(getFile!!, false)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_PERMISSION_CODE)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        customStatusBar()
        initButton()
    }

    public override fun onResume() {
        super.onResume()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture,
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takeImage() {
        // Check if image capture process is already in progress
        if (isImageCaptureInProgress) {
            return
        }

        // Disable the capture button and other UI elements during image capture
        disableUIElements()

        // Set the flag to indicate that the image capture process is in progress
        isImageCaptureInProgress = true

        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    startProcessCamera(photoFile, true)
                    // Reset the flag and enable the capture button when the image capture process is completed
                    isImageCaptureInProgress = false
                    enableUIElements()

                    // Unbind the camera from the lifecycle to freeze the camera preview
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(this@CameraActivity)
                    cameraProviderFuture.addListener({
                        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                        cameraProvider.unbindAll()

                        // Play the shutter sound effect
                        playShutterSound()
                    }, ContextCompat.getMainExecutor(this@CameraActivity))
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@CameraActivity, "Failed to capture image", Toast.LENGTH_SHORT)
                        .show()
                    // Reset the flag and enable the capture button in case of an error
                    isImageCaptureInProgress = false
                    enableUIElements()
                }
            },
        )
    }

    private fun disableUIElements() {
        binding.imageViewCameraButtonCapture.isEnabled = false
        binding.imageViewCameraGallery.isEnabled = false
        binding.imageViewCameraSwitch.isEnabled = false
    }

    private fun enableUIElements() {
        binding.imageViewCameraButtonCapture.isEnabled = true
        binding.imageViewCameraGallery.isEnabled = true
        binding.imageViewCameraSwitch.isEnabled = true
    }

    private fun playShutterSound() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.shutter_sound)
        mediaPlayer.setOnCompletionListener {
            // Release the MediaPlayer resources after playing the sound effect
            mediaPlayer.release()
        }
        mediaPlayer.start()
    }

    private fun allPermissionsGranted(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Select Image")
        launcherIntentGalley.launch(chooser)
    }

    fun startProcessCamera(photoFile: File, fromCamera: Boolean) {
        val intent = Intent(this, PredictDiseaseProcessActivity::class.java)
        val scanImage = CameraModel(photoFile)

        if (fromCamera) {
            PredictDiseaseProcessActivity.IS_BACK_CAMERA = cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA

            intent.putExtra(PredictDiseaseProcessActivity.CAMERA_X_RESULT, scanImage)
        } else {
            intent.putExtra(PredictDiseaseProcessActivity.GALLERY_RESULT, scanImage)
        }

        startActivity(intent)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                showToast("Permission not granted by user")
                finish()
            }
        }
    }

    private fun initButton() {
        utility.setButtonClickAnimation(binding.imageViewCameraButtonCapture, R.anim.button_click_animation) {
            takeImage()
        }

        utility.setButtonClickAnimation(binding.imageViewCameraSwitch, R.anim.button_click_animation) {
            cameraSelector = if (cameraSelector.equals(CameraSelector.DEFAULT_BACK_CAMERA)) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
            startCamera()
        }

        utility.setButtonClickAnimation(binding.imageViewCameraGallery, R.anim.button_click_animation) {
            startGallery()
        }
    }

    @Suppress("DEPRECATION")
    private fun customStatusBar() {
        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG = CameraActivity::class.java.simpleName
        private const val REQUEST_PERMISSION_CODE = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(Manifest.permission.CAMERA).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}
