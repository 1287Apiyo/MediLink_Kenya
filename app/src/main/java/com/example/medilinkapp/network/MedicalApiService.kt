package com.example.medilinkapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

// API Request Model
data class AIRequest(val contents: List<Content>)

// API Response Model
data class AIResponse(val candidates: List<Candidate>)

data class Content(val parts: List<Part>)
data class Part(val text: String)
data class Candidate(val content: Content)

interface MedicalApiService {

    @Headers("Content-Type: application/json")
    @POST("models/gemini-pro:generateContent")
    suspend fun getMedicalResponse(
        @Query("key") apiKey: String,  // API Key passed as query param
        @Body request: AIRequest
    ): AIResponse

    companion object {
        private const val BASE_URL = "https://generativelanguage.googleapis.com/v1/"

        fun create(): MedicalApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MedicalApiService::class.java)
        }
    }
}
