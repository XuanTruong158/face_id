package com.example.face_id.teacher.network

import com.example.face_id.teacher.model.ClassItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TeacherApi {
    // Lấy “Lớp của tôi”
    @GET("teachers/{id}/classes")
    suspend fun getClasses(
        @Path("id") teacherId: String,
        @Query("term") term: String? = null
    ): Response<List<ClassItem>>
}
