package com.example.medilinkapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MedicalApiService {

    @POST("v1beta/models/gemini-pro:generateContent")
    suspend fun getMedicalResponse(
        @Header("Authorization") apiKey: String,
        @Body request: AIRequest
    ): GeminiResponse

    companion object {
        fun create(): MedicalApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://generativelanguage.googleapis.com/") // Gemini's URL
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(MedicalApiService::class.java)
        }
    }
}
