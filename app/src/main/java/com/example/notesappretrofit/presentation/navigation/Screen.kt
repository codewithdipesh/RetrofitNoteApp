package com.example.notesappretrofit.presentation.navigation

 sealed class Screen(val route :String){
     object Home : Screen("home")
     object Login : Screen("login")
     object Register : Screen("register")
     object AddorEdit : Screen("add_edit")

}
