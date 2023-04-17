package com.example.notes.utils

import android.content.Context
import com.example.notes.utils.Constants.USER_TOKEN
import com.example.notes.utils.Constants.preference_file_key
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private var sharedPreferences =
        context.getSharedPreferences(preference_file_key, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = sharedPreferences.edit().putString(USER_TOKEN, token).apply()
    }

    fun getToken() : String? {
        return sharedPreferences.getString(USER_TOKEN, null)
    }
}