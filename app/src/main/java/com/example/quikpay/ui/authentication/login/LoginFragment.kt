package com.example.quikpay.ui.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quikpay.R
import com.example.quikpay.databinding.FragmentLoginBinding
import com.example.quikpay.ui.authentication.AuthViewModel
import com.example.quikpay.ui.authentication.AuthViewModelFactory
import com.example.quikpay.ui.authentication.ProgressListener
import com.example.quikpay.utils.startHomeActivity
import kotlinx.android.synthetic.main.fragment_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class LoginFragment : Fragment(), ProgressListener, KodeinAware {
    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity(), factory).get(AuthViewModel::class.java)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.authViewModel = viewModel
        viewModel.progressListener = this
        binding.lifecycleOwner = this

        viewModel.startLogin.observe(requireActivity(), Observer {
            if (it == true) {
                viewModel.setLoginValues(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                )
            }
        })
        return binding.root
    }

    override fun onStarted() {
        progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        progressbar.visibility = View.GONE
        requireActivity().startHomeActivity()
    }

    override fun onFailure(message: String) {
        progressbar.visibility = View.GONE
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        viewModel.user?.let {
            requireActivity().startHomeActivity()
        }
    }

}
