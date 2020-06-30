package com.example.quikpay.ui.authentication.signup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import java.io.IOException


class SignupOneFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private lateinit var viewModel: AuthViewModel
    private val PICK_IMAGE_REQUEST = 234
    private lateinit var imageView: ImageView
    private var filePath: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity(), factory).get(AuthViewModel::class.java)
        val binding: FragmentSignupOneBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_signup_one, container, false)
        binding.authViewModel = viewModel
        binding.lifecycleOwner = this
        imageView = binding.imageView5

        viewModel.navigateToPageTwo.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val name = binding.fullName.text.toString()
                val mobile = binding.phoneNo.text.toString()
                if (mobile.isNotEmpty() && name.isNotEmpty()) {
                    if (mobile.length in 10..14) {
                        this.findNavController()
                            .navigate(SignupOneFragmentDirections.actionSignupFirstFragmentToSignupSecondFragment())
                        viewModel.setSignupPageOneValues(
                            name,
                            mobile,
                            filePath
                        )
                    } else {
                        binding.phoneNo.error = "Enter a valid phone number"
                        binding.phoneNo.requestFocus()
                    }
                } else {
                    if (mobile.isEmpty()) {
                        binding.phoneNo.error = "Phone number cannot be empty"
                        binding.phoneNo.requestFocus()
                    }
                    if (name.isEmpty()) {
                        binding.fullName.error = "Full name cannot be empty"
                        binding.fullName.requestFocus()
                    }
                }
                viewModel.doneNavigateToSignupNextPage()
            }
        })
        viewModel.uploadPhoto.observe(requireActivity(), Observer {
            if (it == true) {
                binding.imageView6.visibility = View.GONE
                imageView.setBackgroundResource(R.drawable.round_outline)
                showFileChooser()
                imageView.clipToOutline = true
                viewModel.doneUploadPhoto()
            }
        })
        return binding.root
    }


    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Profile Picture"),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
