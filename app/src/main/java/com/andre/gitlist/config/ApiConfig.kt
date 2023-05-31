package com.andre.gitlist.config

import com.andre.gitlist.service.GithubApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val BASE_URL = "https://api.github.com/"

class ApiConfig private constructor() {
    fun getService(): GithubApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "token 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")
                .build()
            chain.proceed(request)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(GithubApiService::class.java)
    }

    companion object {
        @Volatile
        private var INSTANCE: ApiConfig? = null

        fun getInstance(): ApiConfig =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiConfig().also { INSTANCE = it }
            }
    }
}
