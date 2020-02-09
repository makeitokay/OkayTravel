package com.example.okaytravel.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.views.LoginView

@InjectViewState
class LoginPresenter: MvpPresenter<LoginView>() {
    fun doLogin() {
        viewState.startSigningIn()
    }
}