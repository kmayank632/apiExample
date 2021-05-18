package com.example.apipractice.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.apipractice.R
import com.example.apipractice.application.StorePreferencesss
import com.example.apipractice.databinding.FragmentLoginBinding
import com.example.apipractice.databinding.FragmentSplashBinding
import com.example.apipractice.login.LoginVM

class SplashFragment : Fragment() {

    lateinit var binding: FragmentSplashBinding
    lateinit var storePreferencesss: StorePreferencesss

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_splash, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storePreferencesss = StorePreferencesss(requireContext())
        storePreferencesss.getUser.asLiveData().observe(requireActivity(), {
            if ( it == "PATIENT") {
                Handler().postDelayed({
                    findNavController().navigate(R.id.action_splashFragment_to_profileFragment)
                }, 2000)
            }
            else{
                Handler().postDelayed({
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }, 2000)
            }
        })
    }

}