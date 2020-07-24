package com.example.quikpay.ui.pool

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.quikpay.ui.pool.openpools.OpenPoolsFragment
import com.example.quikpay.ui.pool.pendingpools.PendingPoolsFragment
import com.example.quikpay.ui.pool.requestpool.RequestPoolFragment


class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> PendingPoolsFragment.newInstance()
            2 -> OpenPoolsFragment.newInstance()
            else -> RequestPoolFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            1 -> "Pending"
            2 -> "Open Pools"
            else -> "Request"
        }
    }
}