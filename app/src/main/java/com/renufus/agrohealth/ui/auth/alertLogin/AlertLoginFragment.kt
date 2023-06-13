package com.renufus.agrohealth.ui.auth.alertLogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.renufus.agrohealth.databinding.FragmentAlertLoginBinding
import com.renufus.agrohealth.ui.auth.login.LoginActivity
import com.renufus.agrohealth.utility.GeneralUtility

class AlertLoginFragment : Fragment() {
    private lateinit var binding: FragmentAlertLoginBinding
    private val utility = GeneralUtility()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAlertLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAlertLogin.setOnClickListener {
            utility.moveToAnotherActivity(requireContext(), LoginActivity::class.java)
        }
    }
}
