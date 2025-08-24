package com.example.face_id.auth.model

data class LoginResponse(
    val token: String,
    val user: UserInfo
)