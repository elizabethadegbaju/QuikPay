package com.example.quikpay.ui.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.quikpay.ProgressListener
import com.example.quikpay.R
import com.example.quikpay.databinding.FragmentEditProfileBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class EditProfileFragment : Fragment(), KodeinAware, ProgressListener {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    override val kodein by kodein()
    private val factory: EditProfileViewModelFactory by instance()
    private lateinit var viewModel: EditProfileViewModel
    private lateinit var binding: FragmentEditProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this, factory).get(EditProfileViewModel::class.java)
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_edit_profile,
            container,
            false
        )
        binding.editProfileViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.fetchUserDetails()
        binding.profilePicture.clipToOutline = true
        viewModel.userDetails.observe(viewLifecycleOwner, Observer {
            Glide.with(this)
                .load(it.photoURL)
                .apply(RequestOptions().placeholder(R.drawable.ic_round_person_24))
                .into(binding.profilePicture)
        })

        return binding.root
    }

    override fun onStarted() {
        binding.progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        binding.progressbar.visibility = View.GONE
    }

    override fun onFailure(message: String) {
        binding.progressbar.visibility = View.GONE
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

}
