package com.example.quikpay.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quikpay.R
import com.example.quikpay.databinding.FragmentWelcomeBinding
import com.example.quikpay.ui.authentication.login.LoginActivity

class WelcomeFragment : Fragment() {

    companion object {
        fun newInstance() = WelcomeFragment()
    }

    private lateinit var viewModel: WelcomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(WelcomeViewModel::class.java)
        val binding: FragmentWelcomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false)
        binding.welcomeViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                viewModel.doneNavigating()
            }
        })
        viewModel.image.observe(viewLifecycleOwner, Observer {
            binding.imageIntro.setImageResource(it)
        })
        viewModel.introTitle.observe(viewLifecycleOwner, Observer {
            binding.textIntroTitle.setText(it)
        })
        viewModel.introText.observe(viewLifecycleOwner, Observer {
            binding.textIntroText.setText(it)
        })
        viewModel.square1.observe(viewLifecycleOwner, Observer {
            binding.imageStep1.setImageResource(it)
        })
        viewModel.square2.observe(viewLifecycleOwner, Observer {
            binding.imageStep2.setImageResource(it)
        })
        viewModel.square3.observe(viewLifecycleOwner, Observer {
            binding.imageStep3.setImageResource(it)
        })
        viewModel.getStartedVisible.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.btnGetStarted.visibility = View.VISIBLE
                binding.btnNext.visibility = View.GONE
                binding.btnSkip.visibility = View.GONE
            } else {
                binding.btnGetStarted.visibility = View.GONE
                binding.btnNext.visibility = View.VISIBLE
                binding.btnSkip.visibility = View.VISIBLE
            }
        })
        return binding.root
    }
}
