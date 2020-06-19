package com.example.quikpay.ui.authentication.forgotpassword

import android.content.Intent
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
import com.example.quikpay.databinding.FragmentForgotPasswordBinding
import com.example.quikpay.ui.authentication.login.LoginActivity


class ForgotPasswordFragment : Fragment() {

    companion object {
        fun newInstance() =
            ForgotPasswordFragment()
    }

    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(ForgotPasswordViewModel::class.java)
        val binding: FragmentForgotPasswordBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false)
        binding.forgotPasswordViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToVerification.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToVerificationCodeFragment())
                viewModel.doneNavigating()
            }
        })
        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                viewModel.doneNavigating()
            }
        })

        return binding.root
    }

}
