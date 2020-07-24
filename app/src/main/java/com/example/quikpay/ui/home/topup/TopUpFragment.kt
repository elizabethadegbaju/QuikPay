package com.example.quikpay.ui.home.topup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quikpay.R
import com.example.quikpay.databinding.FragmentTopUpBinding
import com.example.quikpay.ui.home.HomeViewModel
import com.example.quikpay.ui.home.HomeViewModelFactory
import com.example.quikpay.utils.startHomeActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class TopUpFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: HomeViewModelFactory by instance()
    private lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentTopUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(requireActivity(), factory).get(HomeViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_up, container, false)
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        homeViewModel.startTopUp.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val amount = binding.textAmount.text.toString()
                homeViewModel.topUp(amount)
            }
        })

        homeViewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                requireActivity().startHomeActivity()
                homeViewModel.onNavigateToHome()
            }
        })

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment TopUpFragment.
         */
        @JvmStatic
        fun newInstance() =
            TopUpFragment()
    }
}