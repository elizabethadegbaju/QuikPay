package com.example.quikpay

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import com.example.quikpay.utils.startLoginActivity
import com.example.quikpay.utils.startWelcomeActivity


class SplashActivity : AppCompatActivity() {
    var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return

        handler.postDelayed(3000) {
            val firstTime = sharedPref.getBoolean("com.example.quikpay.FIRST_TIME", true)
            if (firstTime) {
                with(sharedPref.edit()) {
                    putBoolean("com.example.quikpay.FIRST_TIME", false)
                    commit()
                }
                this.startWelcomeActivity()
            } else {
                this.startLoginActivity()
            }
            finish()
        }
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
