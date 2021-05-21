package com.example.apipractice.view.activity

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.apipractice.application.MyApplication
import com.example.apipractice.view.listener.ResourceProvider

class MainActivityVM : ViewModel() {
    /** Initialize ResourceProvider  */
    val resourceProvider: ResourceProvider = ResourceProvider(MyApplication.getApplication())
    /** ToolBar Title */
    val toolBarTitle = ObservableField("")
}