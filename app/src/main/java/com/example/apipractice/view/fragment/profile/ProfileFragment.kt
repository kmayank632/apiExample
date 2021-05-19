package com.example.apipractice.view.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.apipractice.R
import com.example.apipractice.application.MyApplication
import com.example.apipractice.basemodel.Constants
import com.example.apipractice.utills.StorePreferencesss
import com.example.apipractice.databinding.FragmentMyProfileBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentMyProfileBinding
    lateinit var viewModel: ProfileVM
    lateinit var storePreferencesss: StorePreferencesss

    companion object{
        val LOGOUT = "LOGOUT"

    }

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

        // call API
        viewModel.getProfileData()

        onClick()
    }

    //Set clickListener
    fun onClick() {

        //Logout Button Click
        binding.logout.setOnClickListener {

            // clear dataStore token and userType
            GlobalScope.launch {
                storePreferencesss.storeValue(StorePreferencesss.Token ,"")
                storePreferencesss.storeValue(StorePreferencesss.User ,"")
            }
            val bundle = bundleOf().apply {
                putString(Constants.KEY , LOGOUT)
            }
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment,bundle)
        }
    }

}