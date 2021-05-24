package com.example.apipractice.view.fragment.home

import androidx.lifecycle.ViewModelProvider
import com.example.apipractice.R
import com.example.apipractice.basemodel.BaseViewModel
import com.example.apipractice.datamodel.BannerData
import com.example.apipractice.datamodel.Url

class BannerHomeItemViewModel(val bannerImage: Url) : BaseViewModel() {
    /**
     * @return Layout file i.e R.layout.view_design
     */
    override val viewType: Int = R.layout.inflate_banner_layout

}