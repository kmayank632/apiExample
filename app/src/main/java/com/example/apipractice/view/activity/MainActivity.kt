package com.example.apipractice.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.apipractice.R
import com.example.apipractice.application.MyApplication
import com.example.apipractice.databinding.ActivityMainBinding
import com.example.apipractice.utills.StorePreferencesss
import com.google.android.material.bottomnavigation.BottomNavigationView

//TODO Remove Warnings and Write Proper Comments
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var storePreferencesss: StorePreferencesss

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        storePreferencesss = StorePreferencesss(this)
        observeData()
        navController = Navigation.findNavController(this, R.id.fragment_container)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener)

    }

    fun observeData() {

        val app = MyApplication.getApplication()
        storePreferencesss.readValue(StorePreferencesss.Token).asLiveData().observe(this,
            {
                app.setToken(it)
            }
        )
        storePreferencesss.readValue(StorePreferencesss.User).asLiveData().observe(this,
            {
                app.setUserType(it)
            }
        )
        storePreferencesss.readValue(StorePreferencesss.DEMAND_PROFILE_DATA).asLiveData()
            .observe(this,
                {
                    if (it != null) {
                        app.setProfileData(it)
                    }
                }
            )


    }

    private val navListener: BottomNavigationView.OnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                when (item.itemId) {
                    R.id.home ->
                        if (navController.currentDestination?.id != R.id.homeFragment) {
                            navController
                                .navigate(R.id.homeFragment)
                            return true
                        }
                    R.id.myProfile ->
                        if (navController.currentDestination?.id != R.id.profileFragment) {
                            navController
                                .navigate(R.id.profileFragment)
                            return true
                        }


                }
                return true

            }

        }

}