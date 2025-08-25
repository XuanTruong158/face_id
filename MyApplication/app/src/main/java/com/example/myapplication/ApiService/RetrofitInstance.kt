package com.example.myapplication.ApiService

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.6:8000/api/") // Nếu dùng emulator, localhost là 10.0.2.2
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiUser: UserAPI by lazy {
        retrofit.create(UserAPI::class.java)
    }
    // api mon
    val apiMonhoc: MonhocApi by lazy {
        retrofit.create(MonhocApi::class.java)
    }
    val apiLophocphan: LophocphanAPI by lazy {
        retrofit.create(LophocphanAPI::class.java)
    }
}
