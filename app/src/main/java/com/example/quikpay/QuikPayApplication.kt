package com.example.quikpay

import android.app.Application
import com.example.quikpay.data.repositories.GeneralRepository
import com.example.quikpay.data.repositories.TransactionRepository
import com.example.quikpay.data.repositories.UserRepository
import com.example.quikpay.data.repositories.firebase.FirebaseSource
import com.example.quikpay.ui.authentication.AuthViewModelFactory
import com.example.quikpay.ui.editprofile.EditProfileViewModelFactory
import com.example.quikpay.ui.home.HomeViewModelFactory
import com.example.quikpay.ui.reportissue.ReportIssueViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class QuikPayApplication : Application(), KodeinAware {

    companion object {
        lateinit var instance: QuikPayApplication private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@QuikPayApplication))

        bind() from singleton { FirebaseSource() }
        bind() from singleton { UserRepository(instance()) }
        bind() from singleton { GeneralRepository(instance()) }
        bind() from singleton { TransactionRepository(instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { GodViewModelFactory(instance()) }
        bind() from provider { EditProfileViewModelFactory(instance()) }
        bind() from provider { ReportIssueViewModelFactory(instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }

    }
}