package com.example.apipractice.view.fragment.splash

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.apipractice.R
import com.example.apipractice.application.MyApplication
import com.example.apipractice.basemodel.Constants
import com.example.apipractice.databinding.FragmentSplashBinding
import com.example.apipractice.utills.StorePreferencesss

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

        storePreferencesss.readValue(StorePreferencesss.User).asLiveData().observe(requireActivity(),{
            if ( it == Constants.USER_TYPE.PATIENT) {

                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(R.id.action_splashFragment_to_profileFragment)
                }, 2000)
            }
            else{
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }, 2000)
            }
        })
    }

}