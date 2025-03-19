package com.example.medilinkapp.ui.components

import android.Manifest
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraPreviewView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Check if CAMERA permission is granted
    val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
    if (permissionStatus != PermissionChecker.PERMISSION_GRANTED) {
        Log.e("CameraPreview", "Camera permission not granted")
        return
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            // Create a PreviewView to display the camera feed
            val previewView = PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
                // Switch to PERFORMANCE mode (try PERFORMANCE if COMPATIBLE is not showing preview)
                implementationMode = PreviewView.ImplementationMode.PERFORMANCE
            }
            // Obtain the camera provider
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    Log.d("CameraPreview", "CameraProvider obtained.")

                    // Build the preview use case
                    val preview = Preview.Builder().build().apply {
                        setSurfaceProvider(previewView.surfaceProvider)
                    }

                    // Use the back camera
                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                    // Unbind any existing use cases before binding the new one
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                    Log.d("CameraPreview", "Camera use case bound successfully.")
                } catch (exc: Exception) {
                    Log.e("CameraPreview", "Use case binding failed", exc)
                }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        }
    )
}
