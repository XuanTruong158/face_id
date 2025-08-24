package com.example.face_id.core.network

import com.example.face_id.face_registration.model.FaceDescriptorRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FaceApi {
    @POST("api/face-descriptors")
    suspend fun registerFace(@Body body: FaceDescriptorRequest): Response<Any>
}