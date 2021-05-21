package com.example.apipractice.view.listener


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.apipractice.basemodel.BaseModel

class EventListener  {
    private val showHideToolBarLD = MutableLiveData<BaseModel>()


    /**
     * Notify to Update Tool Bar Visibility Status
     * */
    fun showToolBar(show: BaseModel) {
        showHideToolBarLD.postValue(show)
    }


    /**
     * @return Tool Bar Visibility Live Data
     * */
    fun getToolBarVisibilityLD() = showHideToolBarLD

}