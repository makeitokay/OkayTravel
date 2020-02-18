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

    @Column(name = "anonymous")
    var anonymous: Boolean = false

    fun trips(): MutableList<TripModel> {
        return getMany(TripModel::class.java, "user")
    }

    fun updateTrigger() {
        this.commits = this.commits + 1
        this.save()
    }

    constructor(username: String, email: String?, passwordHash: String?, avatar: String?, accessToken: String?, anonymous: Boolean = false) {
        this.username = username
        this.email = email
        this.passwordHash = passwordHash
        this.avatar = avatar
        this.accessToken = accessToken
        this.anonymous = anonymous
    }

    constructor()
}