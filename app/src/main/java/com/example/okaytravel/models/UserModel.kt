package com.example.okaytravel.models

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import java.util.*

@Table(name = "Users")
class UserModel(
    id: Int,
    username: String,
    email: String,
    avatar: String,
    accessToken: String,
    lastUpdateDatetime: Date
) : Model() {
    @Column(name = "id", unique = true)
    var id: Int? = id

    @Column(name = "username", unique = true)
    var username: String? = username

    @Column(name = "email", unique = true)
    var email: String? = email

    @Column(name = "avatar")
    var avatar: String? = avatar

    @Column(name = "accessToken")
    var accessToken: String? = accessToken

    @Column(name = "lastUpdateDatetime")
    var lastUpdateDatetime: Date? = lastUpdateDatetime

}