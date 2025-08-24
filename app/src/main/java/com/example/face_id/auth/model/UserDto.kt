package com.example.face_id.auth.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")     val id: String? = null,
    @SerializedName("_id")    val mongoId: String? = null,
    @SerializedName("userId") val legacyId: String? = null,
    @SerializedName("name")   val name: String? = null,
    @SerializedName("role")   val role: String? = null,
    @SerializedName("faceRegistered") val faceRegistered: Boolean = false
) {
    val resolvedId: String? get() = id ?: mongoId ?: legacyId
}