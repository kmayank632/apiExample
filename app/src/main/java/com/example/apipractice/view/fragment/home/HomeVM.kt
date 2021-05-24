package com.example.apipractice.view.fragment.home

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.apipractice.application.MyApplication
import com.example.apipractice.basemodel.BaseCommonAdapter
import com.example.apipractice.basemodel.Constants
import com.example.apipractice.datamodel.BannerListModel
import com.example.apipractice.datamodel.Url
import com.example.apipractice.network.NetworkModule
import com.example.apipractice.view.listener.ResourceProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeVM : ViewModel() {
    var bannerAdapterList: ArrayList<BannerHomeItemViewModel> = ArrayList()
    var bannerAdapter: BaseCommonAdapter<BannerHomeItemViewModel>? = null

    /** Initialize ResourceProvider */
    val resourceProvider: ResourceProvider = ResourceProvider(MyApplication.getApplication())
    var snakbarMessage = ObservableField("")

    /** GET Banner API*/
    fun getBanner() {
        NetworkModule.retrofit.getBannerList()
            .enqueue(object : Callback<BannerListModel> {
                override fun onResponse(
                    call: Call<BannerListModel>,
                    response: Response<BannerListModel>
                ) {
                    /** If Successful*/
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            if (response.body()?.status == true) {
                                response.body()?.data?.let { dataItem ->
                                    run loop@{
                                        dataItem.forEach {
                                            if (it._id == Constants.BANNER_TYPE.HOME) {
                                                if (!it.urls.isNullOrEmpty()) {
                                                    it.urls.forEach { url ->
                                                        if (url.en?.trim()?.isNotEmpty() == true) {

                                                            /** Store Item In The BannerAdapterList*/
                                                            bannerAdapterList.add(
                                                                BannerHomeItemViewModel(
                                                                    Url(url.en, url.hi)
                                                                )
                                                            )
                                                        }
                                                    }
                                                }
                                                return@loop
                                            }
                                        }
                                    }
                                    bannerAdapter?.notifyDataSetChanged()
                                }
                            } else {
                                /**Set Snackbar for data is null*/
                                snakbarMessage.set("${response.body()?.message}")
                            }
                        }
                    }

                }

                override fun onFailure(
                    call: Call<BannerListModel>,
                    t: Throwable
                ) {
                    /**Set Snackbar for onFailure*/
                    snakbarMessage.set("${t.cause}")
                }

            })
    }
}