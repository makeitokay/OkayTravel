package com.example.okaytravel.api.models.okaytravelserver

data class CreateUserBody(
    val username: String,
    val email: String,
    val passwordHash: String
)