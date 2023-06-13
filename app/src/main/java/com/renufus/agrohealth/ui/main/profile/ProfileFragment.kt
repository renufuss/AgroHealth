package com.renufus.agrohealth.ui.main.profile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        getProfile()
    }

    private fun getProfile() {
        viewModel.refreshToken()

        viewModel.errorTokenStatus.observe(viewLifecycleOwner) { errorTokenStatus ->
            showLoading(true)
            if (!errorTokenStatus) {
                viewModel.getProfile()
                viewModel.errorStatus.observe(viewLifecycleOwner) { errorDataStatus ->
                    if (!errorDataStatus) {
                        viewModel.profile.observe(viewLifecycleOwner) { profile ->
                            binding.textViewProfileEmail.text = profile.data.email
                            binding.textViewProfileUsername.text = profile.data.username
                        }
                    } else {
                        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    logout()
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
                binding.progressProfileLoading.visibility = View.VISIBLE
                binding.imageViewProfileImageProfile.visibility = View.INVISIBLE
                binding.textViewProfileEmail.visibility = View.INVISIBLE
                binding.textViewProfileUsername.visibility = View.INVISIBLE
                binding.imageViewProfileButtonLogout.visibility = View.INVISIBLE
            }

            else -> {
                binding.progressProfileLoading.visibility = View.GONE
                binding.imageViewProfileImageProfile.visibility = View.VISIBLE
                binding.textViewProfileEmail.visibility = View.VISIBLE
                binding.textViewProfileUsername.visibility = View.VISIBLE
                binding.imageViewProfileButtonLogout.visibility = View.VISIBLE
            }
        }
    }

    private fun logout() {
        viewModel.userPreferences.setStatusLogin(false)
        viewModel.userPreferences.setEmail("emailData")
        viewModel.userPreferences.setUsername("usernameData")
        viewModel.userPreferences.setToken("tokenApi")

        utility.moveToAnotherActivity(requireContext(), LoginActivity::class.java)
    }
}
