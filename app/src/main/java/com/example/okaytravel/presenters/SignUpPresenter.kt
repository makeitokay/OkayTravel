package com.example.okaytravel.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.views.SignUpView

@InjectViewState
class SignUpPresenter: MvpPresenter<SignUpView>() {
    fun doSignUp() {
        viewState.showMessage("There will be signing up soon...")
    }
}