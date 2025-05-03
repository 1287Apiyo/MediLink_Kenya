package com.example.medilinkapp.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class SymptomsRequest(val symptoms: String)
data class SymptomPrediction(val condition: String)

interface SymptomCheckerApi {
    @POST("/predict")
    fun predictCondition(@Body request: SymptomsRequest): Call<SymptomPrediction>
}
