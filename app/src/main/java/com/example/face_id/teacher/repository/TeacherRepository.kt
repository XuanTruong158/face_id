package com.example.face_id.teacher.repository

import com.example.face_id.core.network.ApiClient
import com.example.face_id.teacher.model.ClassItem
import com.example.face_id.core.network.TeacherApi
import retrofit2.Response
import retrofit2.HttpException
import java.io.IOException
import kotlinx.coroutines.CancellationException

class TeacherRepository {

    private val api: TeacherApi by lazy {
        ApiClient.retrofit.create(TeacherApi::class.java)
    }

    // Gọi theo semester + year
    suspend fun getClasses(
        teacherId: String,
        semester: String? = null,
        year: Int? = null
    ): Result<List<ClassItem>> {
        return safeCall { api.getClasses(teacherId, term = null, semester = semester, year = year) }
    }

    // (Tuỳ chọn) Gọi theo term nếu backend của bạn dùng "term"
    suspend fun getClasses(
        teacherId: String,
        term: String?
    ): Result<List<ClassItem>> {
        return safeCall { api.getClasses(teacherId, term = term, semester = null, year = null) }
    }

    // ---- Helper: PHẢI là suspend ----
    private suspend fun <T> safeCall(call: suspend () -> Response<T>): Result<T> {
        return try {
            val res = call()
            if (!res.isSuccessful) {
                val msg = res.errorBody()?.string()?.take(300)
                Result.failure(Exception("HTTP ${res.code()} ${res.message()}${if (msg.isNullOrBlank()) "" else ": $msg"}"))
            } else {
                val body = res.body()
                if (body == null) Result.failure(IllegalStateException("Empty response body"))
                else Result.success(body)
            }
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
