package com.example.medilinkapp.ui.components

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionWrapper(content: @Composable () -> Unit) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    when {
        cameraPermissionState.status.isGranted -> {
            // Permission is granted, show the content
            content()
        }
        cameraPermissionState.status.shouldShowRationale -> {
            // Explain why the permission is needed and provide a button to request it
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("This app requires camera access for video calling.", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Grant Permission")
                }
            }
        }
        else -> {
            // First time request or user previously denied without 'Don't ask again'
            LaunchedEffect(key1 = cameraPermissionState) {
                cameraPermissionState.launchPermissionRequest()
            }
        }
    }
}
