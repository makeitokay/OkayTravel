package com.example.okaytravel.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.SharedPrefHelper
import com.example.okaytravel.helpers.UsersApiHelper
import com.example.okaytravel.isEmailValid
import com.example.okaytravel.isInternetAvailable
import com.example.okaytravel.sha256
import com.example.okaytravel.views.SignUpView

@InjectViewState
class SignUpPresenter(private val context: Context): MvpPresenter<SignUpView>() {

    private val usersApiHelper = UsersApiHelper()
    private val usersDBHelper: UsersDatabaseHelper = UsersDatabaseHelper()
    private val sessionSharedPref = SharedPrefHelper("session", context)

    lateinit var signedUpAccessToken: String

    fun startSignUp(username: String, email: String, password: String, passwordAgain: String) {
        if (!isInternetAvailable(context)) {
            viewState.showMessage(R.string.noInternetConnection)
            return
        }

        viewState.startSigningUp()
        if (!validateInputData(username, email, password, passwordAgain)) {
            viewState.endSigningUp()
            return
        }

        usersApiHelper.createUser(username, email, password.sha256(), {
            if (!it.error) {
                signedUpAccessToken = it.accessToken!!
                if (usersDBHelper.getUserByUsername("Anonymous")?.commits!! > 0) {
                    viewState.showSyncAnonymUserDialog()
                }
                else {
                    endSignUpWithoutSyncAnonym(username, email, password)
                }

            } else {
                viewState.showMessage(it.message!!)
                viewState.endSigningUp()
            }
        }, {
            viewState.showMessage(R.string.unknownError)
            viewState.endSigningUp()
        })
    }

    fun endSignUpWithoutSyncAnonym(username: String, email: String, password: String) {
        usersDBHelper.createUser(username, email, password.sha256(), signedUpAccessToken, false)
        viewState.endSigningUp()
        sessionSharedPref.setCurrentUser(username)
        viewState.startHome()
    }

    fun endSignUpWithSyncAnonym(username: String, email: String, password: String) {
        usersDBHelper.replaceAnonymousWithNewUser(username, email, password.sha256(), signedUpAccessToken)
        usersDBHelper.createAnonymousUser()
        viewState.endSigningUp()
        sessionSharedPref.setCurrentUser(username)
        viewState.startHome()
    }

    private fun validateInputData(username: String, email: String, password: String, passwordAgain: String): Boolean {
        if (username.length !in 3..20) {
            viewState.showMessage(R.string.invalidUsernameSize)
            viewState.focusUsername()
            return false
        }
        if (username == "Anonymous") {
            viewState.showMessage(R.string.usernameError)
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