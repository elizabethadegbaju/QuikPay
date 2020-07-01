package com.example.quikpay.ui.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.quikpay.R
import com.example.quikpay.databinding.FragmentEditProfileBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class EditProfileFragment : Fragment(), KodeinAware {

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

        return binding.root
    }

}
