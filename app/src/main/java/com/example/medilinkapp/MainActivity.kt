 package com.example.medilinkapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.medilinkapp.ui.navigation.AppNavGraph
import com.example.medilinkapp.ui.theme.MedilinkAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch to the normal theme before setting the content
        setTheme(R.style.Theme_MedilinkApp)
        super.onCreate(savedInstanceState)
        setContent {
            MedilinkAppTheme {
                AppNavGraph(context = this)
            }
        }
    }
}
