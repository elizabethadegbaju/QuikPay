package com.example.quikpay.ui.authentication.signup

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
import com.example.quikpay.databinding.FragmentVerificationCodeBinding
import com.example.quikpay.ui.authentication.forgotpassword.ForgotPasswordViewModel

import com.example.quikpay.ui.authentication.login.LoginActivity

/**
 * A simple [Fragment] subclass.
 */
class VerificationCodeFragment : Fragment() {

    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(ForgotPasswordViewModel::class.java)
        val binding: FragmentVerificationCodeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_verification_code, container, false)
        binding.forgotPasswordViewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.navigateToNewPassword.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(VerificationCodeFragmentDirections.actionVerificationCodeFragmentToNewPasswordFragment())
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
