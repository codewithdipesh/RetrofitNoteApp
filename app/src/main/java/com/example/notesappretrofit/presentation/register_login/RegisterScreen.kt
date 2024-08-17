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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.notesappretrofit.presentation.navigation.Screen
import com.example.notesappretrofit.presentation.register_login.elements.ClickableUnderlinedText
import com.example.notesappretrofit.presentation.register_login.elements.CustomButton
import com.example.notesappretrofit.presentation.register_login.elements.FormField
import com.example.notesappretrofit.presentation.register_login.elements.FormFieldWithIcon
import com.example.notesappretrofit.presentation.register_login.viewmodels.RegisterLoginViewModel
import com.example.notesappretrofit.presentation.register_login.viewmodels.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    viewModel: RegisterLoginViewModel,
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

    var showEmptyUsernameError by remember {
        mutableStateOf(false)
    }
    var showEmptyPasswordError by remember {
        mutableStateOf(false
        )
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(isAuthorized){
        if(isAuthorized){
            navController.navigate(Screen.Home.route){
                popUpTo(Screen.Register.route){ inclusive = true }
            }
        }
    }
    LaunchedEffect(authState){
        when(authState){
            is UiState.Error -> {
                scope.launch {
                    Toast.makeText(context,(authState as UiState.Error).error,Toast.LENGTH_SHORT).show()
                }
                viewModel.updateUiStateToNormal()
            }
            is UiState.Success ->{
                scope.launch {
                    Toast.makeText(context,"Registered Successfully",Toast.LENGTH_SHORT).show()
                }
                viewModel.updateUiStateToNormal()
            }
            else-> {}
        }
    }





   Scaffold(
       containerColor = background_color
   ) {
         Box(modifier = Modifier
             .padding(it)
             .padding(horizontal = 38.dp)
             .padding(bottom = 50.dp)
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
                  text = "BEGIN YOUR JOURNEY",
                  fontSize = 14.sp,
                  color = colorResource(id = R.color.gray)
                  )
              Spacer(modifier = Modifier.height(16.dp))
              Text(
                  text = "Sign Up Now",
                  fontSize = 40.sp,
                  color = text_color
              )
              Spacer(modifier = Modifier.height(30.dp))
              FormField(
                  field = "USERNAME",
                  onChange = {
                         viewModel.updateRegisterUsername(it)
                  },
                  textColor = text_color,
                  isEnabled = isFormEnabled
              )
              //empty username
              if(showEmptyUsernameError){
                  Text(
                      text = "username can't be empty",
                      fontSize = 14.sp,
                      color = Color.Red
                  )
                  Spacer(modifier = Modifier.height(4.dp))
              }else{
                  Spacer(modifier = Modifier.height(18.dp))
              }
              FormFieldWithIcon(
                  field = "PASSWORD",
                  onChange = {
                         viewModel.updateRegisterPassword(it)
                  },
                  inputColor = text_color,
                  isEnabled = isFormEnabled
              )
              //empty password
              if(showEmptyPasswordError){
                  Text(
                      text = "password can't be empty",
                      fontSize = 14.sp,
                      color = Color.Red
                  )
                  Spacer(modifier = Modifier.height(10.dp))
              }else{
                  Spacer(modifier = Modifier.height(20.dp))
              }

              Row (modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically){
                  CustomButton(text = "REGISTER") {
                      if (authState !is UiState.Loading) {
                          //check empty inputs
                          if (formState.registerUsername.isEmpty() || formState.registerPassword.isEmpty()) {
                              //both empty
                              if (formState.registerUsername.isEmpty() && formState.registerPassword.isEmpty()) {
                                  scope.launch {
                                      showEmptyUsernameError = true
                                      showEmptyPasswordError = true
                                      delay(600)
                                      showEmptyUsernameError = false
                                      showEmptyPasswordError = false
                                  }
                              } else if (formState.registerUsername.isEmpty()) {
                                  scope.launch {
                                      showEmptyUsernameError = true
                                      delay(600)
                                      showEmptyUsernameError = false
                                  }
                              } else {
                                  scope.launch {
                                      showEmptyPasswordError = true
                                      delay(600)
                                      showEmptyPasswordError = false
                                  }
                              }
                          }
                          //everything is fine
                          else {
                              viewModel.register(
                                  formState.registerUsername,
                                  formState.registerPassword
                              )
                          }
                      }
                  }
                  ClickableUnderlinedText(
                      text = "HAVE AN ACCOUNT?",
                      onClick = {
                          if(authState !is UiState.Loading){
                              navController.navigate(Screen.Login.route)
                          }
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





