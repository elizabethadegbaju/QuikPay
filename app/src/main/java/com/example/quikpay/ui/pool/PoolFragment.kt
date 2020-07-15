package com.example.quikpay.ui.pool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.quikpay.databinding.FragmentPoolBinding
import com.google.android.material.tabs.TabLayout
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class PoolFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val factory: PoolViewModelFactory by instance()
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var poolViewModel: PoolViewModel
    private lateinit var tabLayout: TabLayout
    lateinit var binding: FragmentPoolBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        poolViewModel = ViewModelProvider(this, factory).get(PoolViewModel::class.java)
        binding = FragmentPoolBinding.inflate(inflater, container, false)
        viewPager = binding.viewPagerPool
        tabLayout = binding.tabLayoutPool
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(viewPager)

        return binding.root
    }
}