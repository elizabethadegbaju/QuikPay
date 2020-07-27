package com.example.quikpay.ui.sendmoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.quikpay.databinding.FragmentSendMoneyBinding
import com.example.quikpay.utils.startHomeActivity
import com.google.android.material.tabs.TabLayout
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 * Use the [SendMoneyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SendMoneyFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: SendMoneyViewModelFactory by instance()
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var sendMoneyViewModel: SendMoneyViewModel
    private lateinit var tabLayout: TabLayout
    lateinit var binding: FragmentSendMoneyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sendMoneyViewModel =
            ViewModelProvider(requireActivity(), factory).get(SendMoneyViewModel::class.java)
        binding = FragmentSendMoneyBinding.inflate(inflater, container, false)
        tabLayout = binding.tabLayoutSendMoney
        viewPager = binding.viewPagerSendMoney
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = 2
        tabLayout.setupWithViewPager(viewPager)

        sendMoneyViewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                requireActivity().startHomeActivity()
                sendMoneyViewModel.onNavigateToHome()
            }
        })

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SendMoneyFragment.
         */
        @JvmStatic
        fun newInstance() =
            SendMoneyFragment()
    }
}