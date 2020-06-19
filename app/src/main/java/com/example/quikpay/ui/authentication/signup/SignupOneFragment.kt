package com.example.quikpay.ui.authentication.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quikpay.R
import com.example.quikpay.databinding.FragmentSignupOneBinding
import com.example.quikpay.ui.authentication.AuthViewModel
import com.example.quikpay.ui.authentication.AuthViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class SignupOneFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity(), factory).get(AuthViewModel::class.java)
        val binding: FragmentSignupOneBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_signup_one, container, false)

        viewModel.navigateToPageTwo.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(SignupOneFragmentDirections.actionSignupFirstFragmentToSignupSecondFragment())
                viewModel.setSignupPageOneValues(
                    binding.fullName.text.toString(),
                    binding.phoneNo.text.toString()
                )
                viewModel.doneNavigateToSignupNextPage()
            }
        })

        binding.authViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}
