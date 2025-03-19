package com.example.medilinkapp.model

import androidx.annotation.DrawableRes
import com.example.medilinkapp.R

data class Doctor(

    val name: String = "",
    val specialization: String = "",
    val experience: String = "",
    @DrawableRes val drawableId: Int = R.drawable.doctor1 // Default image
)
