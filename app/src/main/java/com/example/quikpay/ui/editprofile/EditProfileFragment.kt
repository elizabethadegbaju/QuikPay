package com.example.quikpay.ui.editprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import java.io.IOException

class EditProfileFragment : Fragment(), KodeinAware, ProgressListener {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    override val kodein by kodein()
    private val factory: EditProfileViewModelFactory by instance()
    private lateinit var viewModel: EditProfileViewModel
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var profilePicture: ImageView
    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 980

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
        profilePicture = binding.profilePicture
        viewModel.fetchUserDetails()
        binding.profilePicture.clipToOutline = true
        viewModel.progressListener = this

        viewModel.userDetails.observe(viewLifecycleOwner, Observer {
            Glide.with(this)
                .load(it.photoURL)
                .apply(RequestOptions().placeholder(R.drawable.ic_round_person_24))
                .into(binding.profilePicture)
        })
        viewModel.uploadPhoto.observe(requireActivity(), Observer {
            if (it == true) {
                profilePicture.setBackgroundResource(R.drawable.round_outline)
                showFileChooser()
                profilePicture.clipToOutline = true
                viewModel.doneUploadPhoto()
            }
        })
        viewModel.startUpdate.observe(requireActivity(), Observer {
            if (it == true) {
                val name = binding.textName.text.toString()
                val mobile = binding.textPhoneNo.text.toString()
                val email = binding.textEmail.text.toString()
                val password = binding.textPassword.text.toString()
                if (name.isEmpty() || mobile.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    if (mobile.isEmpty()) {
                        binding.textPhoneNo.error = "Phone number cannot be empty"
                        binding.textPhoneNo.requestFocus()
                    }
                    if (email.isEmpty()) {
                        binding.textEmail.error = "Email cannot be empty"
                        binding.textEmail.requestFocus()
                    }
                    if (name.isEmpty()) {
                        binding.textName.error = "Full name cannot be empty"
                        binding.textName.requestFocus()
                    }
                    if (password.isEmpty()) {
                        binding.textPassword.error = "Please enter a password"
                        binding.textPassword.requestFocus()
                    }
                } else {
                    if (mobile.length !in 10..14) {
                        binding.textPhoneNo.error = "Enter a valid phone number"
                        binding.textPhoneNo.requestFocus()
                    } else {
                        viewModel.setValues(name, mobile, filePath, email, password)
                        viewModel.updateUserDetails()
                    }
                }
            }
        })

        return binding.root
    }

    override fun onStarted() {
        binding.progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        binding.progressbar.visibility = View.GONE
        Toast.makeText(requireActivity(), "Profile successfully updated.", Toast.LENGTH_LONG).show()
    }

    override fun onFailure(message: String) {
        binding.progressbar.visibility = View.GONE
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
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
                profilePicture.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
