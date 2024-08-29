package com.example.notesappretrofit.presentation.home
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.notesappretrofit.presentation.home.elements.AnimatedShimmer
import com.example.notesappretrofit.presentation.home.elements.BiometricPromptManager
import com.example.notesappretrofit.presentation.home.elements.ConnectionLostScreen
import com.example.notesappretrofit.presentation.home.elements.HomeView
import com.example.notesappretrofit.presentation.home.elements.ServerErrorScreen
import com.example.notesappretrofit.presentation.home.viewModel.HomeViewModel
import com.example.notesappretrofit.presentation.home.viewModel.UiState
import com.example.notesappretrofit.presentation.navigation.AuthViewModel
import com.example.notesappretrofit.presentation.navigation.Screen
import kotlinx.coroutines.launch


@Composable
fun Home(
    viewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    navController: NavController,
    promptManager:BiometricPromptManager

) {
    
    val uistate by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val graphicsLayer = rememberGraphicsLayer()

    //for the first time after register or login ,
    //fetching the data
    LaunchedEffect(Unit){
        val token = viewModel.getToken()
        if (token != null) {
            viewModel.fetchData(token)
        }
    }

    LaunchedEffect(uistate){
        if(uistate is UiState.Unauthorized){
            scope.launch {
                //delete the token and isAuthorized otherwise it will return to homescreen
                authViewModel.resetAuthState()
                navController.navigate(Screen.Login.route){
                    popUpTo(Screen.Home.route){inclusive= true}
                }
                Toast.makeText(context,"Unauthorized",Toast.LENGTH_SHORT).show()
                viewModel.updateUiStateToNormal()
            }
        }
        if (uistate is UiState.Error){
            scope.launch{
                Toast.makeText(context,(uistate as UiState.Error).error,Toast.LENGTH_SHORT).show()
            }
            viewModel.updateUiStateToNormal()
        }
    }

    when (uistate) {
        is UiState.Loading,UiState.Unauthorized -> AnimatedShimmer()
        is UiState.NoInternet -> ConnectionLostScreen()
        is UiState.ServerError -> ServerErrorScreen()
        else ->{
            HomeView(viewModel = viewModel,navController = navController,graphicsLayer = graphicsLayer,promptManager=promptManager)
        }
    }
}





