package com.renufus.agrohealth.ui.main.forum

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.renufus.agrohealth.R
import com.renufus.agrohealth.adapter.ForumAdapter
import com.renufus.agrohealth.data.model.forum.ForumItem
import com.renufus.agrohealth.databinding.FragmentForumBinding
import com.renufus.agrohealth.ui.main.forum.detail.DetailForumActivity
import com.renufus.agrohealth.utility.GeneralUtility
import com.renufus.agrohealth.utility.uriToFile
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module
import java.io.File

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
}
