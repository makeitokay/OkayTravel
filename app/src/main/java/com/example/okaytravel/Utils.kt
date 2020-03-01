package com.example.okaytravel

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

const val DATETIME_FORMAT = "dd.MM.yyyy HH:mm:ss.SSS"
const val DATE_FORMAT = "dd.MM.yyyy"

fun String.sha256(): String {
    return this.hashWithAlgorithm("SHA-256")
}

private fun String.hashWithAlgorithm(algorithm: String): String {
    val digest = MessageDigest.getInstance(algorithm)
    val bytes = digest.digest(this.toByteArray(Charsets.UTF_8))
    return bytes.fold("", { str, it -> str + "%02x".format(it) })
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

@Suppress("DEPRECATION")
fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }

    return result
}

fun getCurrentDate(): String {
    return parseDate(Date())
}

fun getCurrentDatetime(): String {
    return parseDatetime(Date())
}

fun parseDatetimeString(date: String): Date? {
    val isoFormat = SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault())
    return isoFormat.parse(date)
}

fun parseDateString(date: String): Date? {
    val isoFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    return isoFormat.parse(date)
}

fun parseDatetime(date: Date): String {
    val isoFormat = SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault())
    return isoFormat.format(date)
}

fun parseDate(date: Date): String {
    val isoFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    return isoFormat.format(date)
}

fun uuid(): String {
    return UUID.randomUUID().toString()
}

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity.currentFocus
    if (view == null) view = View(activity)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}