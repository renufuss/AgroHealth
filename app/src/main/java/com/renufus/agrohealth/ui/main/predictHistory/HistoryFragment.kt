package com.renufus.agrohealth.ui.main.predictHistory

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.renufus.agrohealth.R
import com.renufus.agrohealth.adapter.PredictHistoryAdapter
import com.renufus.agrohealth.data.model.predictDisease.predictHistory.PredictHistoryItem
import com.renufus.agrohealth.databinding.FragmentHistoryBinding
import com.renufus.agrohealth.ui.auth.login.LoginActivity
import com.renufus.agrohealth.ui.predictDisease.result.PredictDiseaseResultActivity
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

        checkLogin()
    }

    private fun getHistory() {
        viewModel.refreshToken()

        val layoutErrorNetwork = view?.findViewById<ConstraintLayout>(R.id.layout_history_error_network)
        layoutErrorNetwork?.visibility = View.GONE

        viewModel.errorTokenStatus.observe(viewLifecycleOwner) { errorTokenStatus ->
            showLoading(true)
            if (!errorTokenStatus) {
                viewModel.getHistory()
                viewModel.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
                    showLoading(true)
                    if (!errorStatus) {
                        viewModel.predictHistory.observe(viewLifecycleOwner) { history ->
                            predictHistoryAdapter.add(history.data)
                        }
                        binding.nestedScrollHistory.visibility = View.VISIBLE
                    } else {
                        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                            binding.layoutHistoryErrorNetwork.textViewLayoutErrorNetwork.text = error
                            binding.layoutHistoryErrorNetwork.buttonLayoutErrorNetwork.setOnClickListener {
                                getHistory()
                            }
                            binding.nestedScrollHistory.visibility = View.GONE
                            layoutErrorNetwork?.visibility = View.VISIBLE
                        }
                    }
                }
            } else {
                viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                    binding.layoutHistoryErrorNetwork.textViewLayoutErrorNetwork.text = error
                    binding.layoutHistoryErrorNetwork.buttonLayoutErrorNetwork.text = "Login"
                    binding.layoutHistoryErrorNetwork.buttonLayoutErrorNetwork.setOnClickListener {
                        logout()
                    }
                    binding.nestedScrollHistory.visibility = View.GONE
                    layoutErrorNetwork?.visibility = View.VISIBLE
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({
                showLoading(false)
            }, utility.delayLoading)
        }
    }

    private fun logout() {
        viewModel.userPreferences.setStatusLogin(false)
        viewModel.userPreferences.setToken("tokenApi")

        utility.moveToAnotherActivity(requireContext(), LoginActivity::class.java)
    }

    private fun checkLogin() {
        val loginStatus = viewModel.userPreferences.getStatusLogin()
        val layoutNeedLogin = view?.findViewById<ConstraintLayout>(R.id.layout_history_need_login)

        if (loginStatus) {
            layoutNeedLogin?.visibility = View.GONE
            getHistory()
        } else {
            layoutNeedLogin?.visibility = View.VISIBLE
            binding.layoutHistoryNeedLogin.buttonLayoutNeedLogin.setOnClickListener {
                utility.moveToAnotherActivity(requireContext(), LoginActivity::class.java)
            }

            showLoading(false)
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
                override fun onClick(history: PredictHistoryItem) {
                    val intent = Intent(requireContext(), PredictDiseaseResultActivity::class.java)
                    intent.putExtra(PredictDiseaseResultActivity.DISEASE_DATA, history)
                    startActivity(intent)
                }
            },
        )
    }
}
