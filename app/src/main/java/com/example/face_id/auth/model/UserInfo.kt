package com.example.face_id.auth.model

data class UserInfo(
    val id: String,
    val name: String,
    val role: String,
    val faceRegistered: Boolean
)