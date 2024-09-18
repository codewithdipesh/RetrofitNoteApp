package com.example.notesappretrofit.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.notesappretrofit.presentation.add_edit.AddEditScreen
import com.example.notesappretrofit.presentation.add_edit.viewmodel.AddEditViewModel
import com.example.notesappretrofit.presentation.home.Home
import com.example.notesappretrofit.presentation.home.elements.BiometricPromptManager
import com.example.notesappretrofit.presentation.home.viewModel.HomeViewModel
import com.example.notesappretrofit.presentation.register_login.LoginScreen
import com.example.notesappretrofit.presentation.register_login.RegisterScreen
import com.example.notesappretrofit.presentation.register_login.viewmodels.RegisterLoginViewModel
import com.example.notesappretrofit.ui.theme.backgroungGray

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AppNavigation(
   navController: NavHostController,
   isAuthorized : Boolean,
   registerLoginViewModel :RegisterLoginViewModel,
   authViewModel: AuthViewModel,
   homeViewModel: HomeViewModel,
   noteViewModel : AddEditViewModel,
   promptManager : BiometricPromptManager
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
          LoginScreen(navController = navController , viewModel = registerLoginViewModel, authViewModel = authViewModel)
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
          RegisterScreen(navController = navController, viewModel = registerLoginViewModel, authViewModel =authViewModel)
      }
      composable(Screen.Home.route){
          Home(viewModel = homeViewModel,navController = navController, authViewModel = authViewModel,promptManager=promptManager)
      }
      composable(Screen.AddorEdit.route+"/{id}",
          arguments = listOf(
              navArgument("id"){
                  type = NavType.IntType
                  nullable = false
                  defaultValue = 0
              }
          )
          ){entry->
          val id = if(entry.arguments != null) entry.arguments!!.getInt("id") else 0
         AddEditScreen(
             id = id,
             viewModel = noteViewModel,
             navController = navController
         )
      }
  }


}