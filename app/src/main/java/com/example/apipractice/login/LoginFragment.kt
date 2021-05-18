package com.example.apipractice.login

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
import androidx.navigation.fragment.findNavController
import com.example.apipractice.R
import com.example.apipractice.StorePreferencesss
import com.example.apipractice.databinding.FragmentLoginBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var viewModel: LoginVM
    lateinit var storePreferencesss: StorePreferencesss
    var token = ""
    var user=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        viewModel = ViewModelProvider(this).get(LoginVM::class.java)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storePreferencesss = StorePreferencesss(requireContext())
        storePreferencesss.getUser.asLiveData().observe(requireActivity(), {
            user = it
            if(it == "PATIENT"){
                findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
            }
            Log.e(TAG, "store ${it} ")

        })
        observeData()
        setClick()
    }

    fun setClick() {
        binding.loginButton.setOnClickListener {
            login()
            Log.e(TAG, "mssg6")

        }
        GlobalScope.launch {
            storePreferencesss.setToken(viewModel.token.get().toString())
            storePreferencesss.setUser(viewModel.userType.get().toString())

        }

    }

    fun login() {
        /* Check Username */
        if (viewModel.usernameField.get()?.trim().isNullOrEmpty()) {
            /* Notify User */
            binding.uniqueIdEditText.error = "Please enter medoid"
            binding.passwordEditText.error = null
            return
        }

        viewModel.usernameField.set(viewModel.usernameField.get()?.trim())

        /* Check Password */
        if (viewModel.passwordField.get().isNullOrEmpty()) {
            /* Notify User */
            binding.passwordEditText.error = "Please enter Password"
            binding.uniqueIdEditText.error = null
            return
        }
        if (viewModel.passwordField.get()?.length!! < 8) {
            binding.passwordEditText.error = "Length > 8"
            binding.uniqueIdEditText.error = null
            return
        }

        viewModel.usernameField.set(viewModel.usernameField.get()?.trim())

        /* Login Button View Click */

        viewModel.performLogin()

    }

    fun observeData() {
        storePreferencesss.getToken.asLiveData().observe(requireActivity(), {
            token = it
            Log.e(TAG, "store ${it} ")

        })
        storePreferencesss.getUser.asLiveData().observe(requireActivity(), {
            user = it
            Log.e(TAG, "store ${it} ")

        })
    }

}