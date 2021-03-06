package com.example.okaytravel.helpers

import android.content.Context


class SharedPrefHelper(name: String, context: Context) {
    private val pref = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun getCurrentUser(): String {
        return pref.getString("currentUser", "") ?: return ""
    }

    fun setCurrentUser(username: String) {
        pref.edit().putString("currentUser", username).apply()
    }

    fun removeCurrentUser() {
        pref.edit().putString("currentUser", null).apply()
    }

    fun getHasVisited(): Boolean {
        return pref.getBoolean("hasVisited", false)
    }

    fun setHasVisited() {
        pref.edit().putBoolean("hasVisited", true).apply()
    }

    fun getShowNoMoreRecommend(): Boolean {
        return pref.getBoolean("showNoMoreRecommend", false)
    }

    fun setShowNoMoreRecommend() {
        pref.edit().putBoolean("showNoMoreRecommend", true).apply()
    }
}