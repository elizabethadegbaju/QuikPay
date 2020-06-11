package com.example.quikpay.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.quikpay.R
import com.example.quikpay.databinding.WelcomeFragmentBinding

class WelcomeFragment : Fragment() {

    companion object {
        fun newInstance() = WelcomeFragment()
    }

    private lateinit var viewModel: WelcomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProviders.of(this).get(WelcomeViewModel::class.java)
        val binding: WelcomeFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.welcome_fragment, container, false)
        binding.welcomeViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
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
