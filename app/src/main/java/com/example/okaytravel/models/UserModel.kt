package com.example.okaytravel.models

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.example.okaytravel.getCurrentDatetime
import java.util.*

@Table(name = "Users")
class UserModel : Model {

    @Column(name = "username", unique = true)
    var username: String? = null

    @Column(name = "email", unique = true)
    var email: String? = null

    @Column(name = "passwordHash")
    var passwordHash: String? = null

    @Column(name = "avatar")
    var avatar: String? = null

    @Column(name = "accessToken")
    var accessToken: String? = null

    @Column(name = "commits")
    var commits: Int = 0

    fun trips(): List<TripModel> {
        return getMany(TripModel::class.java, "user")
    }

    fun updateTrigger() {
        println("BEFORE: ${this.commits}")
        this.commits = this.commits + 1
        println("AFTER: ${this.commits}")
        this.save()
    }

    constructor(username: String, email: String, passwordHash: String, avatar: String?, accessToken: String) {
        this.username = username
        this.email = email
        this.passwordHash = passwordHash
        this.avatar = avatar
        this.accessToken = accessToken
    }

    constructor()
}