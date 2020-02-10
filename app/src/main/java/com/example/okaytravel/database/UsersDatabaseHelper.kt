package com.example.okaytravel.database

import com.activeandroid.query.Select
import com.example.okaytravel.models.UserModel
import java.util.*

class UsersDatabaseHelper {
    fun createUser(username: String, email: String, accessToken: String): UserModel {
        var date = Date()
        var user = UserModel(username, email, null, accessToken, date)
        user.save()
        return user
    }

    fun getUserByUsername(username: String): UserModel? {
        val user = Select()
                    .from(UserModel::class.java)
                    .where("username = ?", username)
                    .executeSingle<UserModel>()
        return user
    }

    fun setUserAccessToken(username: String, accessToken: String) {
        var user = getUserByUsername(username)
        user?.accessToken = accessToken
        user?.save()
    }
}