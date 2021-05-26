package com.example.apipractice.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.MultiDex
import com.example.apipractice.datamodel.CustomerProfileModel


class MyApplication : Application() {

    /** Running Activity Instance */
    lateinit var currentActivity: AppCompatActivity

    /** Login Token */
    private var logintoken: String = ""

    /** User Type */
    private var loginUserType: String? = ""

    /** Profile Data */
    private var userProfileData: CustomerProfileModel.Data? = null

    /** get Instance of the activity */
    companion object {
        lateinit var mInstance: MyApplication

        @Synchronized
        fun getApplication(): MyApplication {
            return mInstance
        }

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        /** set Instance */
        mInstance = this

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
                currentActivity = activity as AppCompatActivity
            }

            override fun onActivityStarted(activity: Activity) {
                currentActivity = activity as AppCompatActivity
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                currentActivity = activity as AppCompatActivity
            }

            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity as AppCompatActivity
            }
        })

    }
    /**
     * Store Token
     * */
    fun setToken(token: String?) {
        logintoken = token ?: ""
    }

    /**
     * @return Token
     * */
    fun getToken() = logintoken

    /**
     * store User Type
     * */
    fun setUserType(user: String?) {
        loginUserType = user ?: ""
    }

    /**
     * @return User Type
     * */
    fun getUserType() = loginUserType

    /**
     * store profile data
     * */
    fun setProfileData(profileData: CustomerProfileModel.Data) {
        userProfileData = profileData
    }

    /**
     * @return Profile Data
     * */
    fun getProfileData() = userProfileData
}