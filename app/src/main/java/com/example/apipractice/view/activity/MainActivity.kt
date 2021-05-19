package com.example.apipractice.view.activity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.apipractice.R
import com.example.apipractice.application.MyApplication
import com.example.apipractice.databinding.ActivityMainBinding
import com.example.apipractice.utills.StorePreferencesss

//TODO Remove Warnings and Write Proper Comments
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var storePreferencesss: StorePreferencesss
    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        storePreferencesss = StorePreferencesss(this)
        observeData()
        navController = Navigation.findNavController(this, R.id.fragment_container)
    }

    fun observeData() {

        val app = MyApplication.getApplication()
        storePreferencesss.readValue(StorePreferencesss.Tokenn).asLiveData().observe(this,
            {
                app.setToken(it)
            }
        )
        storePreferencesss.readValue(StorePreferencesss.User).asLiveData().observe(this,
            {
                app.setUserType(it)
            }
        )
    }
}