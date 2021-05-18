package com.example.apipractice.profile

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.apipractice.R
import com.example.apipractice.StorePreferencesss
import com.example.apipractice.databinding.FragmentMyProfileBinding

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentMyProfileBinding
    lateinit var viewModel: ProfileVM
    lateinit var storePreferencesss: StorePreferencesss
    var token = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_my_profile, container, false
        )
        viewModel = ViewModelProvider(this).get(ProfileVM::class.java)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storePreferencesss = StorePreferencesss(requireContext())

        storePreferencesss.getUser.asLiveData().observe(requireActivity(), {
            Log.e(TAG,"mom $it")
        if(it == "PATIENT"){
            viewModel.getProfileData()
        }
        })
    }

}