package com.example.quikpay.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.quikpay.R
import com.example.quikpay.databinding.FragmentHomeBinding
import com.example.quikpay.utils.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class HomeFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: HomeViewModelFactory by instance()
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var tabLayout: TabLayout
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        requireActivity().actionBar?.hide()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.lifecycleOwner = this
        binding.homeViewModel = homeViewModel
        binding.imageUser.clipToOutline = true
        homeViewModel.fetchUserDetails()

        homeViewModel.userDetails.observe(viewLifecycleOwner, Observer {
            binding.textName.text = it.name
            Glide.with(this)
                .load(it.photoURL)
                .apply(RequestOptions().placeholder(R.drawable.ic_round_person_24))
                .into(binding.imageUser)
        })

        viewPager = binding.viewPagerHome
        tabLayout = binding.tabLayoutHome
        viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager);
        return binding.root
    }
}




