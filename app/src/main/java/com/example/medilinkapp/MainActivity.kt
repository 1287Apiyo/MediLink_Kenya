package com.example.medilinkapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import com.example.medilinkapp.navigation.AppNavGraph
import com.example.medilinkapp.ui.theme.MedilinkAppTheme
import com.example.medilinkapp.viewmodel.ConsultationViewModel

class MainActivity : ComponentActivity() {
private val consultationViewModel: ConsultationViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MedilinkApp)

        setContent {
            var darkThemeEnabled by remember { mutableStateOf(false) }

            MedilinkAppTheme(darkTheme = darkThemeEnabled) {
                AppNavGraph(
                    viewModel = consultationViewModel,
                    context = this,
                    darkThemeEnabled = darkThemeEnabled,
                    onToggleTheme = { darkThemeEnabled = !darkThemeEnabled }
                )
            }
        }
    }
}
