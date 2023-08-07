package com.example.kotlinfirebaselearn

import android.util.Patterns

class Utils {
    companion object {
        fun isValidEmail(email: String): Boolean {
            val pattern = Patterns.EMAIL_ADDRESS
            return pattern.matcher(email).matches()
        }
    }
}