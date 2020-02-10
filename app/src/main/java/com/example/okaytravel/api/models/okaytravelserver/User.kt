package com.example.okaytravel.api.models.okaytravelserver

import java.util.*

data class User(
    val username: String,
    val email: String,
    val passwordHash: String,
    val avatar: String?,
    val lastUpdateDatetime: Date?
)