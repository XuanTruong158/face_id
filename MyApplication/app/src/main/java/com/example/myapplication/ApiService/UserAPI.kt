package com.example.myapplication.ApiService

import com.example.myapplication.Model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.*

interface UserAPI {
    @retrofit2.http.GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String): User

    @POST("users")
    suspend fun createUser(@Body user: User): Response<User>

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User): User

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<Unit>
}
