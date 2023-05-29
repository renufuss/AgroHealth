package com.renufus.agrohealth.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        checkLogin()
    }

    private fun checkLogin() {
        val loginStatus = viewModel.userPreferences.getStatusLogin()
        if (!loginStatus) {
            val activity = requireActivity()
            utility.moveToAnotherActivity(activity, LoginActivity::class.java)
        } else {
            binding.textViewProfileEmail.text = viewModel.userPreferences.getEmail()
            binding.textViewProfileUsername.text = viewModel.userPreferences.getUsername()
        }
    }
}
