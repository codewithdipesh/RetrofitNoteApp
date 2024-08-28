package com.example.notesappretrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.notesappretrofit.presentation.add_edit.viewmodel.AddEditViewModel
import com.example.notesappretrofit.presentation.home.viewModel.HomeViewModel
import com.example.notesappretrofit.presentation.navigation.AppNavigation
import com.example.notesappretrofit.presentation.navigation.AuthViewModel
import com.example.notesappretrofit.presentation.navigation.Screen
import com.example.notesappretrofit.presentation.register_login.LoginScreen
import com.example.notesappretrofit.presentation.register_login.viewmodels.RegisterLoginViewModel
import com.example.notesappretrofit.ui.theme.NotesAppRetrofitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val registerLoginViewModel by viewModels<RegisterLoginViewModel>()
            val homeViewModel by viewModels<HomeViewModel>()
            val authViewModel by viewModels<AuthViewModel>()
            val noteViewModel by viewModels<AddEditViewModel>()
            var isAuthChecked by remember { mutableStateOf(false) }
            var isAuthorized by remember { mutableStateOf(false) }
            NotesAppRetrofitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isAuthChecked) {
                        AppNavigation(
                            navController = navController,
                            registerLoginViewModel = registerLoginViewModel,
                            authViewModel = authViewModel,
                            homeViewModel = homeViewModel,
                            isAuthorized = isAuthorized,
                            noteViewModel = noteViewModel
                        )
                    } else {
                        SplashScreen(
                            authViewModel = authViewModel,
                            onAuthCheckComplete = { authStatus ->
                                isAuthChecked = true
                                isAuthorized = authStatus
                            }
                        )
                    }

                }
            }

        }
    }
    }





