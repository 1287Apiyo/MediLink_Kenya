package com.example.medilinkapp.data

import androidx.annotation.DrawableRes

data class Pharmacy(
    val name: String,
    val location: String,
    val services: String,
    val contact: String,
    val website: String,
    @DrawableRes val imageResId: Int // Add this line for image resource
)
