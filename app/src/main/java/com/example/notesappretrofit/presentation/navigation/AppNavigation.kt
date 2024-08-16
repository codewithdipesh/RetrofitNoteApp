package com.example.notesappretrofit.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.notesappretrofit.presentation.home.HomeView
import com.example.notesappretrofit.presentation.register_login.LoginScreen
import com.example.notesappretrofit.presentation.register_login.RegisterScreen
import com.example.notesappretrofit.presentation.register_login.viewmodels.RegisterLoginViewModel

@Composable
fun AppNavigation(
   navController: NavHostController,
   viewModel: RegisterLoginViewModel
) {
    val isAuthorized by viewModel.isAuthorized.collectAsState()

  NavHost(
      navController =navController ,
      startDestination = if(isAuthorized) Screen.Home.route else Screen.Login.route )
  {
      composable(Screen.Login.route){
          LoginScreen(navController = navController , viewModel = viewModel)
      }
      composable(Screen.Register.route){
          RegisterScreen(navController = navController, viewModel = viewModel)
      }
      composable(Screen.Home.route){
          HomeView()
      }
  }
}