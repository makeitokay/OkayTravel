package com.example.okaytravel.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.api.models.okaytravelserver.CreateUserBody
import com.example.okaytravel.api.services.OkayTravelApiService
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.isEmailValid
import com.example.okaytravel.sha256
import com.example.okaytravel.views.SignUpView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class SignUpPresenter: MvpPresenter<SignUpView>() {

    private val apiService: OkayTravelApiService = OkayTravelApiService.create()
    private val usersDBHelper: UsersDatabaseHelper = UsersDatabaseHelper()

    fun doSignUp(username: String, email: String, password: String, passwordAgain: String): Boolean {
        viewState.startSigningUp()
        if (!validateInputData(username, email, password, passwordAgain)) {
            viewState.endSigningUp()
            return false
        }
        val body = CreateUserBody(username, email, password.sha256())
        apiService.createUser(body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({
                if (!it.error) {
                    usersDBHelper.createUser(username, email, it.message)
                    viewState.endSigningUp()
                    viewState.showMessage("User $username was created!")

                } else {
                    viewState.showMessage(it.message)
                }
            }, { error ->
                println(error)
                viewState.showMessage(R.string.unknownError)
                viewState.endSigningUp()
            })
        return true
    }

    private fun validateInputData(username: String, email: String, password: String, passwordAgain: String): Boolean {
        if (username.length !in 3..20) {
            viewState.showMessage(R.string.invalidUsernameSize)
            viewState.focusUsername()
            return false
        }
        if (password != passwordAgain) {
            viewState.showMessage(R.string.notMatchPasswords)
            viewState.focusPasswordAgain()
            return false
        }
        if (password.length < 8) {
            viewState.showMessage(R.string.invalidPasswordSize)
            viewState.focusPassword()
            return false
        }
        if (!email.isEmailValid()) {
            viewState.showMessage(R.string.invalidEmail)
            viewState.focusEmail()
            return false
        }
        return true
    }
}