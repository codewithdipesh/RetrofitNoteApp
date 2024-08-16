package com.example.notesappretrofit.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.notesappretrofit.presentation.home.HomeView
import com.example.notesappretrofit.presentation.register_login.LoginScreen
import com.example.notesappretrofit.presentation.register_login.RegisterScreen
import com.example.notesappretrofit.presentation.register_login.viewmodels.RegisterLoginViewModel
import com.example.notesappretrofit.ui.theme.backgroungGray

@Composable
fun AppNavigation(
   navController: NavHostController,
   isAuthorized : Boolean,
   viewModel:RegisterLoginViewModel
) {
  Box(
      modifier = Modifier
          .fillMaxSize()
          .background(backgroungGray)
  )
  NavHost(
      navController = navController,
      startDestination = if (isAuthorized) Screen.Home.route else Screen.Login.route,
       )
  {
      composable(
          Screen.Login.route,
          enterTransition = {
              fadeIn(animationSpec = tween(100))
          },
          exitTransition = {
              fadeOut(animationSpec = tween(100))
          },
          popEnterTransition = {
              fadeIn(animationSpec = tween(100))
          },
          popExitTransition = {
              fadeOut(animationSpec = tween(100))
          })
      {
          LoginScreen(navController = navController , viewModel = viewModel)
      }
      composable(
          Screen.Register.route,
          enterTransition = {
              fadeIn(animationSpec = tween(100))
          },
          exitTransition = {
              fadeOut(animationSpec = tween(100))
          },
          popEnterTransition = {
              fadeIn(animationSpec = tween(100))
          },
          popExitTransition = {
              fadeOut(animationSpec = tween(100))
          }){
          RegisterScreen(navController = navController, viewModel = viewModel)
      }
      composable(Screen.Home.route){
          HomeView()
      }
  }


}