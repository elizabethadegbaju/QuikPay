package com.example.quikpay.ui.sendmoney

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.quikpay.ui.sendmoney.bank.SendBankFragment
import com.example.quikpay.ui.sendmoney.quikpay.SendQuikPayFragment


class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> SendBankFragment.newInstance()
            else -> SendQuikPayFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            1 -> "Bank Account"
            else -> "Quikpay Account"
        }
    }
}