package com.example.okaytravel.helpers

import android.content.Context


class SharedPrefHelper(name: String, context: Context) {
    private val pref = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun getCurrentUser(): String? {
         return pref.getString("currentUser", null)
    }

    fun setCurrentUser(username: String) {
        pref.edit().putString("currentUser", username).apply()
    }

    fun getHasVisited(): Boolean {
        return pref.getBoolean("hasVisited", false)
    }

    fun setHasVisited() {
        pref.edit().putBoolean("hasVisited", true).apply()
    }
}