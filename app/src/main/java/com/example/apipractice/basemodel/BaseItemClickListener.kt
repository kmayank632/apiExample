package com.example.apipractice.basemodel;

import android.view.View

interface BaseItemClickListener {
    fun onItemClick(view: View, value: BaseViewModel)
}