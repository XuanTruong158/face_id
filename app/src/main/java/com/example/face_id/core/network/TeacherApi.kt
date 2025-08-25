package com.example.face_id.core.network

import com.example.face_id.teacher.model.ClassItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TeacherApi {

    // Backend: GET /api/teachers/{id}/classes
    // Bạn có thể truyền theo kiểu cũ "term" hoặc mới "semester/year".
    @GET("teachers/{id}/classes")
    suspend fun getClasses(
        @Path("id") teacherId: String,
        @Query("term") term: String? = null,          // optional (nếu backend dùng "term")
        @Query("semester") semester: String? = null,  // optional (nếu backend dùng "semester")
        @Query("year") year: Int? = null              // optional
    ): Response<List<ClassItem>>   // <-- NON-NULL Response
}