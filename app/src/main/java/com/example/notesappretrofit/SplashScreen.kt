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
import com.example.notesappretrofit.presentation.home.viewModel.HomeViewModel
import com.example.notesappretrofit.presentation.navigation.AuthViewModel
import com.example.notesappretrofit.presentation.register_login.viewmodels.RegisterLoginViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onAuthCheckComplete: (Boolean) -> Unit,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel
) {
    val viewModel: RegisterLoginViewModel = hiltViewModel()
    val isAuthorized by authViewModel.isAuthorized.collectAsState()

    // Perform the authentication check
    LaunchedEffect(Unit) {
        authViewModel.checkAuthorization()  // Trigger the authorization check
        // Wait until the check is complete
        // Adjust this delay if needed
        delay(1000)
        if(isAuthorized){
            homeViewModel.fetchLocalCache()
        }
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
