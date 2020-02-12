package com.example.okaytravel.database

import com.activeandroid.query.Select
import com.example.okaytravel.models.UserModel
import java.util.*

class UsersDatabaseHelper {
    fun createUser(username: String, email: String, passwordHash: String, accessToken: String): UserModel {
        var date = Date()
        var user = UserModel(username, email, passwordHash, null, accessToken, date)
        user.save()
        return user
    }

    fun getUserByLogin(login: String): UserModel? {
        getUserByUsername(login)?.let {
            return it
        }
        return getUserByEmail(login)
    }

    fun getUserByUsername(username: String): UserModel? {
        return Select()
            .from(UserModel::class.java)
            .where("username = ?", username)
            .executeSingle()
    }

    fun getUserByEmail(email: String): UserModel? {
        return Select()
            .from(UserModel::class.java)
            .where("email = ?", email)
            .executeSingle()
    }

    fun getUserById(id: Int): UserModel? {
        return Select()
            .from(UserModel::class.java)
            .where("id = ?", id)
            .executeSingle()
    }

//    fun setUserAccessToken(username: String, accessToken: String) {
//        var user = getUserByUsername(username)
//        user?.accessToken = accessToken
//        user?.save()
//    }
}