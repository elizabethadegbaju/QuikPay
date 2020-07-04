package com.example.quikpay.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.quikpay.R
import com.example.quikpay.databinding.FragmentHomeBinding

private const val ARG_OBJECT = "object"

class HomeFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        requireActivity().actionBar?.hide()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        viewPager = binding.viewPagerHome
        val tabLayout = binding.tabLayoutHome
        binding.imageUser.clipToOutline = true
        return binding.root
    }
}




