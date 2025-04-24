package com.example.medilinkapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.medilinkapp.navigation.AppNavGraph
import com.example.medilinkapp.ui.theme.MedilinkAppTheme
import com.example.medilinkapp.viewmodel.ConsultationViewModel

class MainActivity : ComponentActivity() {
    // Get the ConsultationViewModel instance
    private val consultationViewModel: ConsultationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Switch to the normal theme before setting the content
        setTheme(R.style.Theme_MedilinkApp)

        setContent {
            MedilinkAppTheme {
                // Pass the viewModel to AppNavGraph
                AppNavGraph(viewModel = consultationViewModel, context = this)
            }
        }
    }
}
