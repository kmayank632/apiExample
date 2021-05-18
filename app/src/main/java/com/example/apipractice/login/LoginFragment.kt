package com.example.apipractice.login

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.apipractice.R
import com.example.apipractice.application.MyApplication
import com.example.apipractice.application.StorePreferencesss
import com.example.apipractice.application.snackBarMessage
import com.example.apipractice.databinding.FragmentLoginBinding
import com.example.apipractice.datamodel.LoginModel
import com.example.apipractice.network.AuthListner
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment(), AuthListner {

    lateinit var binding: FragmentLoginBinding
    lateinit var viewModel: LoginVM
    val app = MyApplication()
    lateinit var storePreferencesss: StorePreferencesss


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
        viewModel.authListner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storePreferencesss = StorePreferencesss(requireContext())
        when (arguments?.getString("KEY")) {
            "LOGOUT" ->
                Snackbar.make(requireContext(),binding.layout,"Logout",Snackbar.LENGTH_SHORT).show()

        }
        setClick()
    }

    fun setClick() {
        binding.loginButton.setOnClickListener {
            viewModel.setLogin()
        }
    }

    override fun onSuccess(loginResponse: LiveData<LoginModel>) {
        loginResponse.observe(this, Observer {
            GlobalScope.launch {
                it.data?.token?.let { it1 -> storePreferencesss.setToken(it1) }
                it.data?.userType?.let { it1 -> storePreferencesss.setUser(it1) }
            }
            Log.e(TAG, "response $it")
            Toast.makeText(
                requireContext(),
                it.message,
                Toast.LENGTH_SHORT
            ).show()
            if(it.message == "Login success" ) {
                findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
            }
        })
    }


}