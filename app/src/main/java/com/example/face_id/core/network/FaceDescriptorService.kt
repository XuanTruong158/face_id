package com.example.face_id.core.network

import com.example.face_id.face_registration.model.FaceDescriptorRequest
import com.example.face_id.face_registration.model.RegisterFaceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FaceDescriptorService {
    @POST("face-descriptors")
    suspend fun registerFace(@Body request: FaceDescriptorRequest): Response<RegisterFaceResponse>
}
