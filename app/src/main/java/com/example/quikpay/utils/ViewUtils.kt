package com.example.quikpay.utils

import android.content.Context
import android.content.Intent
import com.example.quikpay.GodActivity
import com.example.quikpay.ui.authentication.login.LoginActivity
import com.example.quikpay.ui.authentication.signup.SignupActivity
import com.example.quikpay.ui.reportissue.ReportIssueActivity

fun Context.startHomeActivity() =
    Intent(this, GodActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startLoginActivity() =
    Intent(this, LoginActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startSignupActivity() =
    Intent(this, SignupActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startReportIssueActivity() =
    Intent(this, ReportIssueActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }