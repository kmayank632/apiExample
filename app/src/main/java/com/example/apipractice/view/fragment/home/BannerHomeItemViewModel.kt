package com.example.apipractice.view.fragment.home

import com.example.apipractice.R
import com.example.apipractice.basemodel.BaseViewModel
import com.example.apipractice.datamodel.BannerData

class BannerHomeItemViewModel(val bannerImage: BannerData.Url) : BaseViewModel() {

    val placeholder = R.drawable.bg_grey

    /**
     * @return Layout file i.e R.layout.view_design
     */
    override val viewType: Int = R.layout.inflate_banner_layout

}