package com.example.apipractice.network

import android.content.ContentValues.TAG
import android.util.Log
import com.example.apipractice.application.MyApplication
import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val app = MyApplication.getApplication()

        /** Set Header **/
        Log.e(TAG,"tokenheader ${app.getToken()}")
        val request = chain.request()
            .newBuilder()
            .addHeader(
                "Authorization",
                "Bearer ${app.getToken()}"
            )
            .build()
        return chain.proceed(request)
    }
}