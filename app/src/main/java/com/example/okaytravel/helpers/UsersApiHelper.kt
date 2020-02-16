package com.example.okaytravel.helpers

import com.example.okaytravel.api.services.OkayTravelApiService
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.models.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UsersApiHelper {

    private val apiService = OkayTravelApiService.create()
    private val usersDBHelper = UsersDatabaseHelper()

    fun sync(user: UserModel, onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}): Boolean {
        val syncBody = usersDBHelper.serializeUser(user.id) ?: return false
        apiService.sync(syncBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({
                if (it.error == null)
                    usersDBHelper.updateUser(it)
                onSuccess()
            }, { error ->
                println(error)
                onFailure()
            })

        return true
    }

}