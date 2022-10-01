package com.mome.homemome.services.models

data class AuthResponse(
    val context: String,
    val data: Credential,
    val date: String,
    val origin: String,
    val status: String,
    val statusCode: Int
)