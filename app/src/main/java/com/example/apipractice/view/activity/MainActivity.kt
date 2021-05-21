package com.example.apipractice.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.apipractice.R
import com.example.apipractice.application.MyApplication
import com.example.apipractice.databinding.ActivityMainBinding
import com.example.apipractice.utills.StorePreferences
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var viewModel: MainActivityVM
    lateinit var storePreferences: StorePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        storePreferences = StorePreferences(this)
        viewModel = ViewModelProvider(this).get(MainActivityVM::class.java)
        observeData()
        navController = Navigation.findNavController(this, R.id.fragment_container)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener)
        binding.viewModel=viewModel

        /** Bottom Navigation Show and Hide
         * Toolbar Show and Hide
         * */

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.splashFragment -> {
                    binding.toolbar.visibility = View.GONE
                    binding.bottomNavigation.visibility = View.GONE
                }
                R.id.profileFragment -> {
                    binding.toolbar.visibility = View.VISIBLE
                    setToolBarTitle(viewModel.resourceProvider.getString(R.string.my_profile))
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
                R.id.homeFragment ->{
                    binding.toolbar.visibility = View.VISIBLE
                    setToolBarTitle(viewModel.resourceProvider.getString(R.string.home))
                    binding.bottomNavigation.visibility = View.VISIBLE
                }

                R.id.editProfileFragment -> {
                    binding.toolbar.visibility = View.VISIBLE
                    setToolBarTitle(viewModel.resourceProvider.getString(R.string.edit_profile))
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
            }
        }


    }

    /**
     * Change Toolbar Title
     * */
    private fun setToolBarTitle(title: String) {
        viewModel.toolBarTitle.set(title.trim())
    }

    /**
     * Show Toolbar
     * */
    fun hideActionBar() {
        binding.toolbar.visibility = View.GONE
        //  supportActionBar?.hide()
    }


    /** Observe Data*/
    fun observeData() {

        val app = MyApplication.getApplication()

        /** Observe Token From DataStore*/
        storePreferences.readValue(StorePreferences.Token).asLiveData().observe(this,
            {
                app.setToken(it)
            }
        )
        /** Observe User Type From DataStore*/
        storePreferences.readValue(StorePreferences.User).asLiveData().observe(this,
            {
                app.setUserType(it)
            }
        )

        /** Observe  From DataStore*/
        storePreferences.readValue(StorePreferences.DEMAND_PROFILE_DATA).asLiveData()
            .observe(this,
                {
                    if (it != null) {
                        app.setProfileData(it)
                    }
                }
            )

    }

    /** Bottom Navigation Item Click*/

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