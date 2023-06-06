package com.renufus.agrohealth.ui.main.forum

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.renufus.agrohealth.R
import com.renufus.agrohealth.adapter.ForumAdapter
import com.renufus.agrohealth.data.model.forum.ForumItem
import com.renufus.agrohealth.databinding.FragmentForumBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val forumModule = module {
    factory { ForumFragment() }
}

class ForumFragment : Fragment() {

    private lateinit var binding: FragmentForumBinding
    private val viewModel: ForumViewModel by viewModel<ForumViewModel>()

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
        getForumContent()
    }

    private fun getForumContent() {
        viewModel.getForumContent()

        viewModel.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            if (!errorStatus) {
                viewModel.forumContents.observe(viewLifecycleOwner) { forum ->
                    forumAdapter.add(forum.data)

                    Log.d("FORUMFRAGMENTLOG", forum.toString())
                }
            } else {
                viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val forumAdapter by lazy {
        ForumAdapter(
            arrayListOf(),
            object : ForumAdapter.OnAdapterListener {
                override fun onClick(forum: ForumItem) {
                    Toast.makeText(requireContext(), "${forum.id} clicked", Toast.LENGTH_SHORT).show()
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
