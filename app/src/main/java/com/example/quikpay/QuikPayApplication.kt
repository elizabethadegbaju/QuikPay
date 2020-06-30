package com.example.quikpay

import android.app.Application
import com.example.quikpay.data.repositories.UserRepository
import com.example.quikpay.data.repositories.firebase.FirebaseSource
import com.example.quikpay.ui.authentication.AuthViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class QuikPayApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@QuikPayApplication))

        bind() from singleton { FirebaseSource() }
        bind() from singleton { UserRepository(instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { GodViewModelFactory(instance()) }

    }
}