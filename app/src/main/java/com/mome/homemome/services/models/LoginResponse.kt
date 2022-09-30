package com.mome.homemome.services.models

data class LoginResponse(
    val context: String,
    val data: Login,
    val date: String,
    val origin: String,
    val status: String,
    val statusCode: Int
)