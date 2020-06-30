package com.example.quikpay.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quikpay.R
import com.example.quikpay.ui.editprofile.EditProfileFragment

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EditProfileFragment.newInstance())
                .commitNow()
        }
    }
}
