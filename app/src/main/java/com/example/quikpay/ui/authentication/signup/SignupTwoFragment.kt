package com.example.quikpay.ui.authentication.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quikpay.GodActivity
import com.example.quikpay.R
import com.example.quikpay.databinding.FragmentSignupTwoBinding
import com.example.quikpay.ui.authentication.AuthListener
import com.example.quikpay.ui.authentication.AuthViewModel
import com.example.quikpay.ui.authentication.AuthViewModelFactory
import com.example.quikpay.utils.startHomeActivity
import kotlinx.android.synthetic.main.fragment_signup_two.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SignupTwoFragment : Fragment(), AuthListener, KodeinAware {
    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSignupTwoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_signup_two, container, false)
        viewModel = ViewModelProvider(requireActivity(), factory).get(AuthViewModel::class.java)
        binding.authViewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.authListener = this

        viewModel.startSignup.observe(requireActivity(), Observer {
            if (it == true) {
                viewModel.setSignupPageTwoValues(
                    binding.email.text.toString(),
                    binding.password.text.toString(),
                    binding.confirmPassword.text.toString()
                )
            }
        })

        return binding.root
    }

    override fun onStarted() {
        progressbar.visibility = View.VISIBLE
        Intent(activity, GodActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    override fun onSuccess() {
        progressbar.visibility = View.GONE
        activity?.startHomeActivity()
    }

    override fun onFailure(message: String) {
        progressbar.visibility = View.GONE
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}
