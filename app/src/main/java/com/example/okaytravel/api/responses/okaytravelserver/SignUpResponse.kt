package com.example.okaytravel.api.responses.okaytravelserver

data class SignUpResponse(
    val error: Boolean,
    val message: String?,
    val username: String?,
    val accessToken: String?
)