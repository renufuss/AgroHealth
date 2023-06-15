package com.renufus.agrohealth.ui.main.forum

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleOwner
import com.renufus.agrohealth.R
import com.renufus.agrohealth.adapter.ForumAdapter
import com.renufus.agrohealth.databinding.FragmentForumBinding
import com.renufus.agrohealth.ui.auth.login.LoginActivity
import com.renufus.agrohealth.utility.GeneralUtility
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ForumFunction(
    private val viewModel: ForumViewModel,
    private val view: View?,
    private val viewLifecycleOwner: LifecycleOwner,
    private val forumAdapter: ForumAdapter,
    private val binding: FragmentForumBinding,
    private val utility: GeneralUtility,
    private val context: Context,
    private val activity: Activity,

) {
    var getFile: File? = null

    fun getForumContent() {
        viewModel.getForumContent()

        val layoutErrorNetwork = view?.findViewById<ConstraintLayout>(R.id.layout_forum_error_network)
        layoutErrorNetwork?.visibility = View.GONE

        viewModel.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            showLoading(true)
            if (!errorStatus) {
                viewModel.forumContents.observe(viewLifecycleOwner) { forum ->
                    forumAdapter.add(forum.allPost)
                }
                binding.nestedScrollForum.visibility = View.VISIBLE
            } else {
                viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                    binding.layoutForumErrorNetwork.textViewLayoutErrorNetwork.text = error
                    binding.layoutForumErrorNetwork.buttonLayoutErrorNetwork.setOnClickListener {
                        getForumContent()
                    }
                    binding.nestedScrollForum.visibility = View.GONE
                    layoutErrorNetwork?.visibility = View.VISIBLE
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({
                showLoading(false)
            }, utility.delayLoading)
        }
    }

    fun checkLogin() {
        val loginStatus = viewModel.userPreferences.getStatusLogin()

        if (loginStatus) {
            binding.cardForumNewPostBorder.visibility = View.VISIBLE
        } else {
            binding.cardForumNewPostBorder.visibility = View.GONE
        }
    }

    fun initPostForum() {
        val layoutErrorNetwork = view?.findViewById<ConstraintLayout>(R.id.layout_forum_error_network)
        layoutErrorNetwork?.visibility = View.GONE
        binding.nestedScrollForum.visibility = View.VISIBLE
        showLoading(true)
        binding.buttonForumNewPost.isEnabled = false

        val validPost = validationPost()
        if (validPost) {
            runPostForum()
        } else {
            showLoading(false)

            binding.buttonForumNewPost.isEnabled = true
        }
    }

    private fun runPostForum() {
        viewModel.refreshToken()
        val layoutErrorNetwork = view?.findViewById<ConstraintLayout>(R.id.layout_forum_error_network)
        viewModel.errorTokenStatus.observe(viewLifecycleOwner) { errorTokenStatus ->
            if (!errorTokenStatus) {
                val tokenPost = viewModel.userPreferences.getToken()!!
                val descriptionPost = binding.textInputEditTextForumNewPost.text.toString()
                var imagePost: MultipartBody.Part? = null
                if (getFile != null) {
                    val file = reduceFileImage(getFile as File)
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                    imagePost = MultipartBody.Part.createFormData(
                        "image",
                        file.name,
                        requestImageFile,
                    )
                }

                viewModel.newPostForum(tokenPost, descriptionPost, imagePost)

                viewModel.errorForumPostStatus.observe(viewLifecycleOwner) { errorStatus ->
                    if (!errorStatus) {
                        viewModel.forumPost.observe(viewLifecycleOwner) { response ->
                            MotionToast.createToast(
                                activity,
                                "Success",
                                "Successfully uploaded your post",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular),
                            )

                            binding.textInputEditTextForumNewPost.setText("")
                            removeImageNewPost()
                            closeKeyboard(context)
                            getForumContent()
                        }
                    } else {
                        viewModel.errorForumPostMessage.observe(viewLifecycleOwner) { errorMessage ->
                            MotionToast.createToast(
                                activity,
                                "Error",
                                errorMessage,
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular),
                            )
                        }
                    }
                }
            } else {
                viewModel.errorForumPostMessage.observe(viewLifecycleOwner) { error ->
                    binding.layoutForumErrorNetwork.textViewLayoutErrorNetwork.text = error
                    binding.layoutForumErrorNetwork.buttonLayoutErrorNetwork.text = "Login"
                    binding.layoutForumErrorNetwork.buttonLayoutErrorNetwork.setOnClickListener {
                        logout()
                    }
                    binding.nestedScrollForum.visibility = View.GONE
                    layoutErrorNetwork?.visibility = View.VISIBLE
                }
            }

            Handler(Looper.getMainLooper()).postDelayed({
                showLoading(false)
                binding.buttonForumNewPost.isEnabled = true
            }, utility.delayLoading)
        }
    }

    private fun logout() {
        viewModel.userPreferences.setStatusLogin(false)
        viewModel.userPreferences.setToken("tokenApi")

        utility.moveToAnotherActivity(context, LoginActivity::class.java)
    }

    private fun validationPost(): Boolean {
        val descriptionEditText = binding.textInputEditTextForumNewPost

        val description = descriptionEditText.text?.trim().toString()

        if (description.isBlank() || description.length > 250) {
            if (description.isBlank()) {
                binding.textInputLayoutForumNewPost.helperText = "Description is required"
            }

            if (description.length > 250) {
                binding.textInputLayoutForumNewPost.helperText = "Maximum character is 250"
            }

            return false
        } else {
            binding.textInputLayoutForumNewPost.helperText = ""
            return true
        }
    }

    fun removeImageNewPost() {
        getFile = null
        binding.imageViewForumImageNewPost.visibility = View.GONE
        binding.imageViewForumRemoveImageNewPost.visibility = View.GONE
    }

    private fun reduceFileImage(file: File): File {
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

    private fun closeKeyboard(context: Context) {
        val view = (context as AppCompatActivity).currentFocus
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> {
                binding.progressBarForumLoading.visibility = View.VISIBLE
            }

            else -> {
                binding.progressBarForumLoading.visibility = View.INVISIBLE
            }
        }
    }

    companion object {
        const val MAXIMAL_SIZE = 1000000
    }
}
