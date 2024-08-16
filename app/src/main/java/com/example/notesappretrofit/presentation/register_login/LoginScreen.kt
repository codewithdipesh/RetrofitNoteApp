package com.example.notesappretrofit.presentation.register_login
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.example.notesappretrofit.R

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.notesappretrofit.presentation.navigation.Screen
import com.example.notesappretrofit.presentation.register_login.elements.ClickableUnderlinedText
import com.example.notesappretrofit.presentation.register_login.elements.CustomButton
import com.example.notesappretrofit.presentation.register_login.elements.FormField
import com.example.notesappretrofit.presentation.register_login.elements.FormFieldWithIcon
import com.example.notesappretrofit.presentation.register_login.viewmodels.RegisterLoginViewModel
import com.example.notesappretrofit.presentation.register_login.viewmodels.UiState
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: RegisterLoginViewModel ,
    navController: NavController
){
   val bg = R.color.background
   val background_color : Color = colorResource(id = bg)
   val text_color = colorResource(id = R.color.white)

   val authState by viewModel.authState.collectAsState()
   val formState by viewModel.formState.collectAsState()
   val isAuthorized by viewModel.isAuthorized.collectAsState()

   val isFormEnabled by remember {
       derivedStateOf { authState !is UiState.Loading  }
   }

   val scope = rememberCoroutineScope()
   val context = LocalContext.current


   LaunchedEffect(isAuthorized){
       if(isAuthorized){
           //direct home and delete login screen
           navController.navigate(Screen.Home.route){
               popUpTo(Screen.Login.route){inclusive = true}
           }
       }
   }

  LaunchedEffect(authState){
      when (authState) {
          is UiState.Error -> {
              scope.launch {
                  Toast.makeText(context, (authState as UiState.Error).error, Toast.LENGTH_SHORT).show()
              }
          }
          is UiState.Success -> {
              scope.launch {
                  Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
              }
          }
          else -> {}
      }
  }

   Scaffold(
       containerColor = background_color
   ) {
         Box(modifier = Modifier
             .padding(it)
             .padding(horizontal = 24.dp)
             .padding(bottom = 32.dp)
             .fillMaxSize(),
             contentAlignment = Alignment.Center)
         {
          Column(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 8.dp),
              horizontalAlignment = Alignment.Start,
              verticalArrangement = Arrangement.Center
          ) {
              Text(
                  text = "RESUME YOUR JOURNEY",
                  fontSize = 14.sp,
                  color = colorResource(id = R.color.gray)
                  )
              Spacer(modifier = Modifier.height(16.dp))
              Text(
                  text = "WELCOME",
                  fontSize = 40.sp,
                  color = text_color
              )
              Spacer(modifier = Modifier.height(24.dp))
              FormField(
                  name = "USERNAME",
                  onChange = {
                         viewModel.updateLoginUsername(it)
                  },
                  color = text_color,
                  isEnabled = isFormEnabled
              )
              Spacer(modifier = Modifier.height(16.dp))
              FormFieldWithIcon(
                  name = "PASSWORD",
                  onChange = {
                         viewModel.updateLoginPassword(it)
                  },
                  color = text_color,
                  isEnabled = isFormEnabled
              )
              Spacer(modifier = Modifier.height(24.dp))

              Row (modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically){
                  CustomButton(text = "LOGIN"){
                      if(authState !is UiState.Loading){
                          viewModel.login(
                              formState.loginUsername, formState.loginPassword
                          )
                      }
                  }
                  ClickableUnderlinedText(
                      text = "NEW USER?",
                      onClick = {
                            navController.navigate(Screen.Register.route)
                                },
                      color = text_color,
                      size = 16.sp
                  )
              }
          }
             if (authState is UiState.Loading) {
                 Box(
                     modifier = Modifier.fillMaxSize(),
                     contentAlignment = Alignment.Center
                 ) {
                     CircularProgressIndicator(color = text_color)
                 }
             }
        }

   }


}


