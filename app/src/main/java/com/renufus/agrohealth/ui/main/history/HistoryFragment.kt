package com.renufus.agrohealth.ui.main.history

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.renufus.agrohealth.adapter.PredictHistoryAdapter
import com.renufus.agrohealth.data.model.predict.predictHistory.HistoryItem
import com.renufus.agrohealth.databinding.FragmentHistoryBinding
import com.renufus.agrohealth.utility.GeneralUtility
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val historyModule = module {
    factory { HistoryFragment() }
}

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by viewModel<HistoryViewModel>()
    private val utility = GeneralUtility()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewHistoryItem.adapter = predictHistoryAdapter

        getHistory()
    }

    private fun getHistory() {
        viewModel.getHistory()

        viewModel.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            showLoading(true)
            if (!errorStatus) {
                viewModel.predictHistory.observe(viewLifecycleOwner) { history ->
                    predictHistoryAdapter.add(history.data)
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    showLoading(false)
                }, utility.delayLoading)
            } else {
                viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> {
                binding.progressBarHistoryLoading.visibility = View.VISIBLE
            }

            else -> {
                binding.progressBarHistoryLoading.visibility = View.INVISIBLE
            }
        }
    }

    private val predictHistoryAdapter by lazy {
        PredictHistoryAdapter(
            arrayListOf(),
            object : PredictHistoryAdapter.OnAdapterListener {
                override fun onClick(history: HistoryItem) {
                    Toast.makeText(requireContext(), "${history.id} clicked", Toast.LENGTH_SHORT).show()
                }
            },
        )
    }
}
