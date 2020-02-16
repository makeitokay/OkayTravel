package com.example.okaytravel.presenters

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.fragments.SyncAnonymUserDialogFragment
import com.example.okaytravel.api.models.okaytravelserver.CreateUserBody
import com.example.okaytravel.api.services.OkayTravelApiService
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.UsersApiHelper
import com.example.okaytravel.isEmailValid
import com.example.okaytravel.isInternetAvailable
import com.example.okaytravel.sha256
import com.example.okaytravel.views.SignUpView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class SignUpPresenter(private val context: Context): MvpPresenter<SignUpView>() {

    private val usersApiHelper = UsersApiHelper()
    private val usersDBHelper: UsersDatabaseHelper = UsersDatabaseHelper()

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

        if (usersDBHelper.getUserByUsername("Anonymous")?.commits!! > 0) {
            viewState.showSyncAnonymUserDialog()
            return
        }
        signUpWithoutSyncAnonym(username, email, password)
    }

    fun signUpWithoutSyncAnonym(username: String, email: String, password: String) {
        val passwordHash = password.sha256()
        usersApiHelper.createUser(username, email, passwordHash, {
            if (!it.error) {
                usersDBHelper.createUser(username, email, passwordHash, it.accessToken!!)
                viewState.endSigningUp()
                viewState.showMessage("User $username was created!")
                viewState.startLoginView()

            } else {
                viewState.showMessage(it.message!!)
                viewState.endSigningUp()
            }
        }, {
            viewState.showMessage(R.string.unknownError)
            viewState.endSigningUp()
        })
    }

    fun signUpWithSyncAnonym(username: String, email: String, password: String) {
        val passwordHash = password.sha256()
        usersApiHelper.createUser(username, email, passwordHash, {
            if (!it.error) {
                usersDBHelper.replaceAnonymousWithNewUser(username, email, passwordHash, it.accessToken!!)
                usersDBHelper.createAnonymousUser()
                viewState.endSigningUp()
                viewState.showMessage("User $username was created!")
                viewState.startLoginView()

            } else {
                viewState.showMessage(it.message!!)
                viewState.endSigningUp()
            }
        }, {
            viewState.showMessage(R.string.unknownError)
            viewState.endSigningUp()
        })
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