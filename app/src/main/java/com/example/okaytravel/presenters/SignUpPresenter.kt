package com.example.okaytravel.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.api.models.okaytravelserver.CreateUserBody
import com.example.okaytravel.api.services.OkayTravelApiService
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.views.SignUpView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class SignUpPresenter: MvpPresenter<SignUpView>() {

    private val apiService: OkayTravelApiService = OkayTravelApiService.create()
    private val usersDBHelper: UsersDatabaseHelper = UsersDatabaseHelper()

    fun doSignUp(username: String, email: String, passwordHash: String) {
        viewState.startSigningUp()
        val body = CreateUserBody(username, email, passwordHash)
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
    }
}