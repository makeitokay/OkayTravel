package com.example.okaytravel.models

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import java.util.*

@Table(name = "Users")
class UserModel : Model {

    @Column(name = "username", unique = true)
    var username: String? = null

    @Column(name = "email", unique = true)
    var email: String? = null

    @Column(name = "avatar")
    var avatar: String? = null

    @Column(name = "accessToken")
    var accessToken: String? = null

    @Column(name = "lastUpdateDatetime")
    var lastUpdateDatetime: Date? = null

    constructor(username: String, email: String, avatar: String?, accessToken: String, lastUpdateDatetime: Date) {
        this.username = username
        this.email = email
        this.avatar = avatar
        this.accessToken = accessToken
        this.lastUpdateDatetime = lastUpdateDatetime
    }

    constructor()
}