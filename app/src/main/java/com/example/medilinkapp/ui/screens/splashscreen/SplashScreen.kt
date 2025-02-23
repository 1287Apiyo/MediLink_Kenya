package com.example.medilinkapp.ui.screens.splashscreen

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medilinkapp.R
import com.example.medilinkapp.ui.theme.MedilinkAppTheme
import com.example.medilinkapp.viewmodel.SplashViewModel
@Composable
fun SplashScreen(navController: NavController, viewModel: SplashViewModel = viewModel()) {
    LaunchedEffect(true) {
        viewModel.navigateToNextScreen {
            navController.navigate("login") // Navigate after splash
        }
    }

    MedilinkAppTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val hospitalIcon: Painter = painterResource(id = R.drawable.hospital_icon)
            val iconScale by animateFloatAsState(targetValue = 1f, animationSpec = TweenSpec(durationMillis = 1500))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = hospitalIcon,
                    contentDescription = "Hospital Icon",
                    modifier = Modifier
                        .size(120.dp)
                        .graphicsLayer(scaleX = iconScale, scaleY = iconScale)
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "MediLink",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen(navController = rememberNavController()) // Preview requires a NavController
}
