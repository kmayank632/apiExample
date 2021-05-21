package com.example.apipractice.view.fragment.login

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
import androidx.navigation.fragment.findNavController
import com.example.apipractice.R
import com.example.apipractice.application.MyApplication
import com.example.apipractice.basemodel.Constants
import com.example.apipractice.databinding.FragmentLoginBinding
import com.example.apipractice.datamodel.LoginModel
import com.example.apipractice.network.AuthListner
import com.example.apipractice.utills.StorePreferencesss
import com.example.apipractice.view.fragment.profile.ProfileFragment.Companion.LOGOUT
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment(), AuthListner {

    lateinit var binding: FragmentLoginBinding
    lateinit var viewModel: LoginVM
    lateinit var storePreferencesss: StorePreferencesss


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        /** Logout Successfully snackbar */
        when (arguments?.getString(Constants.KEY)) {
            LOGOUT ->
                Snackbar.make(
                    requireContext(),
                    binding.layout,
                    "Logout Successfully",
                    Snackbar.LENGTH_SHORT
                ).show()

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

            //TODO Use Coroutines in ViewModel

            /** Store Token in DataStore*/
            GlobalScope.launch {
                it.data?.token?.let { it1 ->
                    storePreferencesss.storeValue(
                        StorePreferencesss.Token,
                        it1
                    )
                }
                viewModel.app.setToken(it.data?.token)

                /** Store useType in DataStore*/
                it.data?.userType?.let { it1 ->
                    storePreferencesss.storeValue(
                        StorePreferencesss.User,
                        it1
                    )
                }
            }
            Log.e(TAG, "response $it")

            /** Toast Message*/
            Toast.makeText(
                requireContext(),
                it.message,
                Toast.LENGTH_SHORT
            ).show()

            if (it.data?.userType == Constants.USER_TYPE.PATIENT) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        })
    }


}