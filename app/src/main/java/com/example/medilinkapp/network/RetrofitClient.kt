package com.example.medilinkapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.104:5000")
// For emulator use. Use IP address if using a physical device.
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val symptomCheckerApi: SymptomCheckerApi = retrofit.create(SymptomCheckerApi::class.java)
}
