package com.mome.homemome.services

import com.mome.homemome.services.models.LoginPost
import com.mome.homemome.services.models.LoginResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL =
    "http://10.0.2.2:3001/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface HomeApiService{
    @POST("auth/login")
    suspend fun login(@Body loginPost: LoginPost): LoginResponse

}

object HomeApi{
    val retrofitService: HomeApiService by lazy {
        retrofit.create(HomeApiService::class.java)
    }
}