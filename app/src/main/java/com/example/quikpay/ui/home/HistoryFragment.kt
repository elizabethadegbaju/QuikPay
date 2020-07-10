package com.example.quikpay.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.quikpay.databinding.FragmentHistoryBinding
import com.google.android.material.tabs.TabLayout

private const val ARG_COLUMN_COUNT = "column-count"
private const val ARG_TRANSACTION_TYPE = "transaction-type"

class HistoryFragment : Fragment() {
    private var columnCount: String? = null
    private var transactionType: String? = null
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getString(ARG_COLUMN_COUNT)
            transactionType = it.getString(ARG_TRANSACTION_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        viewPager = binding.viewPagerHome
        tabLayout = binding.tabLayoutHome
        viewPagerAdapter =
            ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param columnCount Parameter 1.
         * @param transactionType Parameter 2.
         * @return A new instance of fragment ViewOlderFragment.
         */
        @JvmStatic
        fun newInstance(columnCount: String, transactionType: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_COLUMN_COUNT, columnCount)
                    putString(ARG_TRANSACTION_TYPE, transactionType)
                }
            }
    }
}