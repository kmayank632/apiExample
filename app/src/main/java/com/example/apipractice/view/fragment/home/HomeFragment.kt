package com.example.apipractice.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.apipractice.R
import com.example.apipractice.basemodel.BaseCommonAdapter
import com.example.apipractice.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    /* ViewBinding Variable */
    lateinit var binding: FragmentHomeBinding

    /* ViewModel Variable */
    lateinit var viewModel: HomeVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        viewModel = ViewModelProvider(this).get(HomeVM::class.java)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* Call API*/
        viewModel.getBanner()
        /* Add List to adapter*/
        if (viewModel.bannerAdapter == null) {
            /* Banner Adapter */
            viewModel.bannerAdapter = BaseCommonAdapter(viewModel.bannerAdapterList)
        }
        binding.bannerRecyclerView.adapter = viewModel.bannerAdapter

        /* Attach Indicator to the Recycler View*/
        binding.bannerIndicator.attachToRecyclerView(binding.bannerRecyclerView)
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.bannerRecyclerView)

        /* Call setClick*/
        setClick()
    }

    /** Set Click Listener*/
    private fun setClick() {

    }


}