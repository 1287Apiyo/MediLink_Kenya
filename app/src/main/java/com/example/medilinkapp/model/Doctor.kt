package com.example.medilinkapp.model

import androidx.annotation.DrawableRes

data class Doctor(
    val name: String = "",
    val specialization: String = "",
    val experience: String = "",
    @DrawableRes val drawableId: Int? = null // No default image
)
