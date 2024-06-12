package com.example.flappychickengame.network

import com.example.flappychickengame.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private const val BASE_URL = "https://flappy.jeyy.xyz"

    private val client = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("api-key", BuildConfig.API_KEY)
                .method(original.method(), original.body())
                .build()
            chain.proceed(request)
        }
    }.build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
