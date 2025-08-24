package com.example.face_id.face_registration.model

import com.google.gson.annotations.SerializedName

data class FaceDescriptorRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("descriptors") val descriptors: List<String>,
    @SerializedName("algorithm") val algorithm: String
)