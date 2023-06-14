package com.renufus.agrohealth.ui.main.profile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.renufus.agrohealth.R
import com.renufus.agrohealth.databinding.FragmentProfileBinding
import com.renufus.agrohealth.ui.auth.login.LoginActivity
import com.renufus.agrohealth.utility.GeneralUtility
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val profileModule = module {
    factory { ProfileFragment() }
}
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val utility = GeneralUtility()

    private val viewModel: ProfileViewModel by viewModel<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        utility.setButtonClickAnimation(binding.imageViewProfileButtonLogout, R.anim.button_click_animation) {
            logout()
        }

        checkLogin()
    }

    private fun getProfile() {
        viewModel.refreshToken()

        val layoutErrorNetwork = view?.findViewById<ConstraintLayout>(R.id.layout_profile_error_network)
        layoutErrorNetwork?.visibility = View.GONE

        viewModel.errorTokenStatus.observe(viewLifecycleOwner) { errorTokenStatus ->
            showLoading(true)
            if (!errorTokenStatus) {
                viewModel.getProfile()
                viewModel.errorStatus.observe(viewLifecycleOwner) { errorDataStatus ->
                    if (!errorDataStatus) {
                        viewModel.profile.observe(viewLifecycleOwner) { profile ->
                            binding.textViewProfileEmail.text = profile.data.email
                            binding.textViewProfileUsername.text = profile.data.username
                            binding.imageViewProfileImageProfile.visibility = View.VISIBLE
                        }
                    } else {
                        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                            binding.layoutProfileErrorNetwork.textViewLayoutErrorNetwork.text = error
                            binding.layoutProfileErrorNetwork.buttonLayoutErrorNetwork.setOnClickListener {
                                getProfile()
                            }
                            layoutErrorNetwork?.visibility = View.VISIBLE
                        }
                    }
                }
            } else {
                viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                    binding.layoutProfileErrorNetwork.textViewLayoutErrorNetwork.text = error
                    binding.layoutProfileErrorNetwork.buttonLayoutErrorNetwork.text = "Login"
                    binding.layoutProfileErrorNetwork.buttonLayoutErrorNetwork.setOnClickListener {
                        logout()
                    }
                    layoutErrorNetwork?.visibility = View.VISIBLE
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({
                showLoading(false)
            }, utility.delayLoading)
        }
    }

    private fun checkLogin() {
        val loginStatus = viewModel.userPreferences.getStatusLogin()
        val layoutNeedLogin = view?.findViewById<ConstraintLayout>(R.id.layout_profile_need_login)

        if (loginStatus) {
            layoutNeedLogin?.visibility = View.GONE
            getProfile()
        } else {
            layoutNeedLogin?.visibility = View.VISIBLE
            binding.layoutProfileNeedLogin.buttonLayoutNeedLogin.setOnClickListener {
                utility.moveToAnotherActivity(requireContext(), LoginActivity::class.java)
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> {
                binding.progressProfileLoading.visibility = View.VISIBLE
                binding.imageViewProfileImageProfile.visibility = View.INVISIBLE
                binding.textViewProfileEmail.visibility = View.INVISIBLE
                binding.textViewProfileUsername.visibility = View.INVISIBLE
                binding.imageViewProfileButtonLogout.visibility = View.INVISIBLE
            }

            else -> {
                binding.progressProfileLoading.visibility = View.GONE
                binding.textViewProfileEmail.visibility = View.VISIBLE
                binding.textViewProfileUsername.visibility = View.VISIBLE
                binding.imageViewProfileButtonLogout.visibility = View.VISIBLE
            }
        }
    }

    private fun logout() {
        viewModel.userPreferences.setStatusLogin(false)
        viewModel.userPreferences.setToken("tokenApi")

        utility.moveToAnotherActivity(requireContext(), LoginActivity::class.java)
    }
}
