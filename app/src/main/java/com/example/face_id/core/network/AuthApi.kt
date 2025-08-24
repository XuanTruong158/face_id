package com.example.face_id.core.network

import com.example.face_id.auth.model.LoginRequest
import com.example.face_id.auth.model.LoginResponse
import com.example.face_id.auth.model.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("login")
    suspend fun login(@Body req: LoginRequest): Response<LoginResponse>

    @GET("me")
    suspend fun me(): retrofit2.Response<UserDto>
}