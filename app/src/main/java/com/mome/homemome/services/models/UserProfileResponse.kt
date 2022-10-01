package com.mome.homemome.services.models

data class UserProfileResponse(
    val context: String,
    val data: User,
    val date: String,
    val origin: String,
    val status: String,
    val statusCode: Int
)