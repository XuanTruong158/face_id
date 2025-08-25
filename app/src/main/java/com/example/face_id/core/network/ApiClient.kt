package com.example.face_id.core.network

import com.example.face_id.core.network.TeacherApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiClient {
    // ĐỔI lại theo server của bạn
    private const val BASE_URL = "http://192.168.1.143:8000/api/"

    @Volatile
    private var authToken: String? = null
    fun setAuthToken(token: String?) { authToken = token }

    private val logging: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    private val defaultHeaders = Interceptor { chain ->
        val b = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")

        authToken?.takeIf { it.isNotBlank() }?.let { t ->
            b.addHeader("Authorization", "Bearer $t")
        }
        chain.proceed(b.build())
    }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(defaultHeaders)
            .addInterceptor(logging)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }

   val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val faceApi: FaceDescriptorService by lazy {
        retrofit.create(FaceDescriptorService::class.java)  // <- dùng INSTANCE retrofit
    }

}
