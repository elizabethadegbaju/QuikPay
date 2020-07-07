package com.example.quikpay.utils

import androidx.annotation.StringRes
import com.example.quikpay.QuikPayApplication

object Strings {
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return QuikPayApplication.instance.getString(stringRes, *formatArgs)
    }
}