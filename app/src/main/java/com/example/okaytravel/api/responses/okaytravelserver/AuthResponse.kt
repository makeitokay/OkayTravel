package com.example.okaytravel.api.responses.okaytravelserver

data class AuthResponse(
    val error: Boolean,
    val message: String?,
    val username: String?,
    val accessToken: String?
)