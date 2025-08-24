package com.example.face_id.teacher.repository

import com.example.face_id.core.network.ApiClient
import com.example.face_id.teacher.model.ClassItem
import com.example.face_id.teacher.network.TeacherApi

class TeacherRepository {
    private val api = ApiClient.retrofit.create(TeacherApi::class.java)

    suspend fun getClasses(teacherId: String, term: String? = null)
            : Result<List<ClassItem>> {
        val res = api.getClasses(teacherId, term)
        return if (res.isSuccessful && res.body() != null)
            Result.success(res.body()!!)
        else
            Result.failure(Exception(res.errorBody()?.string() ?: "HTTP ${res.code()}"))
    }
}
