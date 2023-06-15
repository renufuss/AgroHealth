package com.renufus.agrohealth.ui.main.forum

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.renufus.agrohealth.R
import com.renufus.agrohealth.adapter.ForumAdapter
import com.renufus.agrohealth.data.model.forum.ForumItem
import com.renufus.agrohealth.databinding.FragmentForumBinding
import com.renufus.agrohealth.ui.auth.login.LoginActivity
import com.renufus.agrohealth.ui.main.forum.detail.DetailForumActivity
import com.renufus.agrohealth.utility.GeneralUtility
import com.renufus.agrohealth.utility.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

val forumModule = module {
    factory { ForumFragment() }
}

class ForumFragment : Fragment() {

    private lateinit var binding: FragmentForumBinding
    private val viewModel: ForumViewModel by viewModel<ForumViewModel>()
    private val utility = GeneralUtility()
    private var getFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewForumContent.adapter = forumAdapter
        checkLogin()
        getForumContent()

        utility.setButtonClickAnimation(binding.imageViewForumAddImageNewPost, R.anim.button_click_animation) {
            startGallery()
        }

        utility.setButtonClickAnimation(binding.imageViewForumRemoveImageNewPost, R.anim.button_click_animation) {
            removeImageNewPost()
        }

        binding.buttonForumNewPost.setOnClickListener {
            newPostForum()
        }
    }

    private fun getForumContent() {
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

    private fun checkLogin() {
        val loginStatus = viewModel.userPreferences.getStatusLogin()

        if (loginStatus) {
            binding.cardForumNewPostBorder.visibility = View.VISIBLE
        } else {
            binding.cardForumNewPostBorder.visibility = View.GONE
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, requireContext())
                getFile = myFile
                binding.imageViewForumImageNewPost.setImageURI(uri)
            }

            binding.imageViewForumImageNewPost.visibility = View.VISIBLE
            binding.imageViewForumRemoveImageNewPost.visibility = View.VISIBLE
        }
    }

    private fun newPostForum() {
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
                            Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()

                            binding.textInputEditTextForumNewPost.setText("")
                            removeImageNewPost()
                            closeKeyboard(requireContext())
                            getForumContent()
                        }
                    } else {
                        viewModel.errorForumPostMessage.observe(viewLifecycleOwner) { errorMessage ->
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
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

    private fun closeKeyboard(context: Context) {
        val view = (context as AppCompatActivity).currentFocus
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun logout() {
        viewModel.userPreferences.setStatusLogin(false)
        viewModel.userPreferences.setToken("tokenApi")

        utility.moveToAnotherActivity(requireContext(), LoginActivity::class.java)
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

    private fun removeImageNewPost() {
        getFile = null
        binding.imageViewForumImageNewPost.visibility = View.GONE
        binding.imageViewForumRemoveImageNewPost.visibility = View.GONE
    }

    private val forumAdapter by lazy {
        ForumAdapter(
            arrayListOf(),
            object : ForumAdapter.OnAdapterListener {
                override fun onClick(forum: ForumItem) {
                    val intent = Intent(requireContext(), DetailForumActivity::class.java)
                    DetailForumActivity.POST_ID = forum.id
                    startActivity(intent)
                }
            },
            object : ForumAdapter.OnImageClickListener {
                override fun onImageClick(imageUrl: String) {
                    val dialog = Dialog(requireContext())
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setContentView(R.layout.dialog_zoom_image)

                    val imageViewZoom = dialog.findViewById<ImageView>(R.id.imageViewZoom)
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.text_logo)
                        .error(R.drawable.text_logo)
                        .into(imageViewZoom)

                    dialog.show()
                }
            },
        )
    }

    companion object {
        const val MAXIMAL_SIZE = 1000000
    }
}
