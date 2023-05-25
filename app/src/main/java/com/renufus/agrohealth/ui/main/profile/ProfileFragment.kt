package com.renufus.agrohealth.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.renufus.agrohealth.databinding.FragmentProfileBinding
import com.renufus.agrohealth.ui.auth.login.LoginActivity
import com.renufus.agrohealth.utility.GeneralUtility

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private var isLogin: Boolean = false
    private val utility = GeneralUtility()

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
        if (!isLogin) {
            val activity = requireActivity()
            utility.moveToAnotherActivity(activity, LoginActivity::class.java)
        }
    }
}
