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

    lateinit var binding: FragmentHomeBinding
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
        activity?.setTitle(R.string.home)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getBanner()
        if (viewModel.bannerAdapter == null) {

            /* Banner Adapter */
            viewModel.bannerAdapter = BaseCommonAdapter(viewModel.bannerAdapterList)


        }
        binding.bannerRecyclerView.adapter = viewModel.bannerAdapter

        binding.bannerIndicator.attachToRecyclerView(binding.bannerRecyclerView)
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.bannerRecyclerView)
        setClick()
    }

    /** set click Listner*/
    fun setClick() {

    }



}