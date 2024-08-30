package com.example.notesappretrofit.presentation.home.elements

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.example.notesappretrofit.presentation.navigation.Screen


@Composable
fun BiometricResultHandler(
    biometricResult: BiometricPromptManager.BiometricResult?,
    selectedNoteId: MutableState<Int?>,
    navController: NavController,
    enabled :MutableState<Boolean>
) {
    val enrollLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { /* Handle result if necessary */ }
    )

    LaunchedEffect(biometricResult) {
        Log.d("tap under result", selectedNoteId.toString())
        when (biometricResult) {
            is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                if (selectedNoteId.value != null) {
                    Log.d("BiometricResultHandler", "Authenticated successfully with note ID: ${selectedNoteId.value}")
                    navController.navigate(Screen.AddorEdit.route + "/${selectedNoteId.value}")
                }
            }
            is BiometricPromptManager.BiometricResult.AuthenticationNotSet -> {
                if (Build.VERSION.SDK_INT >= 30) {
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                        )
                    }
                    enrollLauncher.launch(enrollIntent)
                }
            }
            else -> Unit // Handle other cases if necessary
        }

    }

}