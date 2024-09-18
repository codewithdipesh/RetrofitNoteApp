package com.example.notesappretrofit.presentation.home
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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


@RequiresApi(Build.VERSION_CODES.P)
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
    val snackBarHostState = remember { SnackbarHostState()}
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
                snackBarHostState.showSnackbar(
                    message = "Unauthorized",
                    duration = SnackbarDuration.Short
                )
                viewModel.updateUiStateToNormal()
            }
        }
        if (uistate is UiState.Error){
            scope.launch{
                snackBarHostState.showSnackbar(
                    message = (uistate as UiState.Error).error ,
                    duration = SnackbarDuration.Short
                )
                viewModel.updateUiStateToNormal()
            }

        }
        if (uistate is UiState.NoInternet){
            scope.launch{
                snackBarHostState.showSnackbar(
                    message = "No Internet Connection",
                    duration = SnackbarDuration.Short
                )
                viewModel.updateUiStateToNormal()
            }
        }
        if(uistate is UiState.ServerError){
            scope.launch{
                snackBarHostState.showSnackbar(
                    message = "Something wrong in Server , Please try again after sometime",
                    duration = SnackbarDuration.Short
                )
                viewModel.updateUiStateToNormal()
            }
        }
    }


    HomeView(viewModel = viewModel,
        navController = navController,
        graphicsLayer = graphicsLayer,
        promptManager=promptManager
    )

}





