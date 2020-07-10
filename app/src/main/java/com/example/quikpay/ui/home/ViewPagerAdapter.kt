package com.example.quikpay.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> TransactionFragment.newInstance(1, "received")
            2 -> TransactionFragment.newInstance(1, "sent")
            else -> TransactionFragment.newInstance(1, "all")
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when (position) {
            0 -> {
                title = "All"
            }
            1 -> {
                title = "Received"
            }
            2 -> {
                title = "Sent"
            }
        }
        return title
    }
}