package com.example.apipractice.view.fragment.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.apipractice.R
import com.example.apipractice.basemodel.Constants
import com.example.apipractice.databinding.FragmentSplashBinding
import com.example.apipractice.utills.StorePreferences

class SplashFragment : Fragment() {

    /* ViewBinding Variable */
    lateinit var binding: FragmentSplashBinding

    /* StorePreferences Variable */
    lateinit var storePreferences: StorePreferences

    /* Time delay Variable */
    private val time : Long = 2 * 1000 /* 2 Seconds */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_splash, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Initialize StorePreference */
        storePreferences = StorePreferences(requireContext())

        /* Observe User type From Database*/
        storePreferences.readValue(StorePreferences.User).asLiveData().observe(viewLifecycleOwner,{

            /* Handler For Delay*/
            Handler(Looper.getMainLooper()).postDelayed({
                if ( it == Constants.USER_TYPE.PATIENT) {
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                }
                else{

                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
            }, time )

        })
    }

}