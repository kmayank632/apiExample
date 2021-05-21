package com.example.apipractice.view.fragment.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.apipractice.basemodel.BaseCommonAdapter
import com.example.apipractice.basemodel.Constants
import com.example.apipractice.datamodel.BannerData
import com.example.apipractice.datamodel.BannerListModel
import com.example.apipractice.network.MyApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeVM : ViewModel() {
    var bannerAdapterList: ArrayList<BannerHomeItemViewModel> = ArrayList()
    var bannerAdapter: BaseCommonAdapter<BannerHomeItemViewModel>? = null

    fun getBanner() {

        MyApi().getBannerList()
            .enqueue(object : Callback<BannerListModel> {
                override fun onResponse(
                    call: Call<BannerListModel>,
                    response: Response<BannerListModel>
                ) {
                    Log.e(TAG,"datapicture ${response.body()?.data}")

                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            response.body()?.data?.let { dataItem ->
                                run loop@{
                                    dataItem.forEach {
                                        if (it._id == Constants.BANNER_TYPE.HOME) {
                                            if (!it.urls.isNullOrEmpty()) {
                                                it.urls.forEach { url ->
                                                    if (url?.en?.trim()?.isNotEmpty() == true) {
                                                        bannerAdapterList.add(BannerHomeItemViewModel(BannerData.Url(url.en,url.hi)))
                                                    }
                                                }
                                            }
                                            return@loop
                                        }
                                    }
                                }
                                bannerAdapter?.notifyDataSetChanged()
                            }
                        }
                    }

                    Log.e(TAG,"adepter ${bannerAdapterList[0]}")
                }

                override fun onFailure(
                    call: Call<BannerListModel>,
                    t: Throwable
                ) {
                    Log.e(TAG,"datapicture ${t.cause}")


                }

            })
    }
}