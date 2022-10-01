package com.mome.homemome.services

import com.mome.homemome.services.models.Credential
import com.mome.homemome.services.models.AuthResponse
import com.mome.homemome.services.models.UserProfileResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

private const val BASE_URL =
    "http://10.0.2.2:3001/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface HomeApiService{
    @POST("auth/login")
    fun auth(@Body credentials: Credential): Call<AuthResponse>

    @GET("users/profile")
    fun getProfile(@Header("Authorization") tokenId : String):Call<UserProfileResponse>
}

object HomeApi{
    val retrofitService: HomeApiService by lazy {
        retrofit.create(HomeApiService::class.java)
    }
}