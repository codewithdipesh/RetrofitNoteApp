package com.example.notesappretrofit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notesappretrofit.presentation.register_login.viewmodels.RegisterLoginViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onAuthCheckComplete: (Boolean) -> Unit
) {
    val viewModel: RegisterLoginViewModel = hiltViewModel()
    val isAuthorized by viewModel.isAuthorized.collectAsState()

    // Perform the authentication check
    LaunchedEffect(Unit) {
        viewModel.checkAuthorization()  // Trigger the authorization check
        // Wait until the check is complete
        // Adjust this delay if needed
        delay(1000)
        onAuthCheckComplete(isAuthorized)
    }

    // Display splash screen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
