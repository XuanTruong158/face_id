package com.example.myapplication.ApiService

import com.example.myapplication.Model.Monhoc
import retrofit2.Response
import retrofit2.http.*

interface MonhocApi {
    @retrofit2.http.GET("courses")
    suspend fun  getMonhocs() : List<Monhoc>

    @GET("courses/{id}")
    suspend fun getMonhoc(@Path("id") id:String): Monhoc

    @POST("courses")
    suspend fun createMonhoc(@Body monhoc: Monhoc) : Response<Monhoc>

    @PUT("courses/{id}:")
    suspend fun  updateMonhoc(@Path ("id") id: String, @Body monhoc:Monhoc) : Response<Monhoc>

    @DELETE("courses/{id}")
    suspend fun deleteMonhoc(@Path("id") id: String) : Response<Unit>

}