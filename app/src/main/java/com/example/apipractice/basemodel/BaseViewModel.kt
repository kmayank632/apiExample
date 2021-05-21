package com.example.apipractice.basemodel

import android.view.View

abstract class BaseViewModel {

    var clickListener: BaseItemClickListener? = null

    open fun onItemClick(view: View) {
        clickListener?.onItemClick(
            view, this
        )
    }

    /**
     * @return Layout file i.e R.layout.view_design
     */
    abstract val viewType: Int
}