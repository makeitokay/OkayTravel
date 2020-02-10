package com.example.okaytravel.api.models.okaytravelserver

data class AuthBody(
    val login: String,
    val passwordHash: String
)