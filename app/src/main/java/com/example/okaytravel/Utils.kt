package com.example.okaytravel

import android.text.TextUtils
import java.security.MessageDigest

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