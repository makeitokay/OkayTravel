package com.example.okaytravel.views

import com.arellomobile.mvp.MvpView

interface SignUpView: MvpView {
    fun showMessage(message: String)
}