package com.example.okaytravel.helpers

import com.example.okaytravel.api.models.okaytravelserver.AuthBody
import com.example.okaytravel.api.models.okaytravelserver.CreateUserBody
import com.example.okaytravel.api.responses.okaytravelserver.SignUpResponse
import com.example.okaytravel.api.responses.okaytravelserver.SyncResponse
import com.example.okaytravel.api.responses.okaytravelserver.UserInfoResponse
import com.example.okaytravel.api.services.OkayTravelApiService
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.models.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UsersApiHelper {

    private val apiService = OkayTravelApiService.create()
    private val usersDBHelper = UsersDatabaseHelper()

    fun sync(
        user: UserModel,
        onSuccess: (syncResponse: SyncResponse) -> Unit = {},
        onFailure: () -> Unit = {}
    ): Boolean {
        val syncBody = usersDBHelper.serializeUser(user.id) ?: return false
        apiService.sync(syncBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.error == null) {
                    if (it.user?.user?.commits != user.commits) usersDBHelper.updateUser(it)
                    onSuccess(it)
                }
                if (it.error == false) onSuccess(it)
                if (it.error == true) {
                    println(it.message)
                    onFailure()
                }
            }, { error ->
                println(error)
                onFailure()
            })

        return true
    }

    fun auth(
        login: String,
        passwordHash: String,
        onSuccess: (userInfoResponse: UserInfoResponse) -> Unit = {},
        onFailure: () -> Unit = {}
    ) {
        val body = AuthBody(login, passwordHash)
        apiService.auth(body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                onSuccess(it)
            }, { error ->
                println(error)
                onFailure()
            })
    }

    fun createUser(
        username: String, email: String, passwordHash: String,
        onSuccess: (signUpResponse: SignUpResponse) -> Unit = {},
        onFailure: () -> Unit = {}
    ) {

        val body = CreateUserBody(username, email, passwordHash)
        apiService.createUser(body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                onSuccess(it)
            }, { error ->
                println(error)
                onFailure()
            })
    }
}