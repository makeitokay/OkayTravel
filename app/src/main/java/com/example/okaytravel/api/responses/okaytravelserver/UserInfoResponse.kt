package com.example.okaytravel.api.responses.okaytravelserver

data class UserInfoResponse(
    val error: Boolean,
    val message: String,
    val user: UserInfo?
)