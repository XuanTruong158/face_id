package com.example.face_id.core.network

import com.example.face_id.auth.model.LoginRequest
import com.example.face_id.auth.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("login")
    suspend fun login(@Body req: LoginRequest): Response<LoginResponse>
}