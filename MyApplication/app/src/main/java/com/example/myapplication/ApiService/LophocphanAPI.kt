package com.example.myapplication.ApiService

import com.example.myapplication.Model.Lophocphan
import retrofit2.Response
import retrofit2.http.*

interface LophocphanAPI {
    @retrofit2.http.GET("classes")
    suspend fun getLophocphans(): List<Lophocphan>

    @retrofit2.http.GET("classes/{id}")
    suspend fun getLophocphan(@Path("id") id: String): Lophocphan

    @retrofit2.http.POST("classes")
    suspend fun createLophocphan(@Body loophocphan: Lophocphan): Response<Lophocphan>

    @retrofit2.http.PUT("classes/{id}")
    suspend fun updateLophocphan(@Path("id") id: String, @Body loophocphan: Lophocphan): Response<Lophocphan>
    @retrofit2.http.DELETE("classes/{id}")
    suspend fun  deleteLophocphan(@Path("id") id: String): Response<Lophocphan>
}