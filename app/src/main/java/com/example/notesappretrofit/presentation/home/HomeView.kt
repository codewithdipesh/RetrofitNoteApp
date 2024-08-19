package com.example.notesappretrofit.presentation.home
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.notesappretrofit.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.notesappretrofit.presentation.home.elements.AnimatedShimmer
import com.example.notesappretrofit.presentation.home.elements.ConnectionLostScreen
import com.example.notesappretrofit.presentation.home.elements.ServerErrorScreen
import com.example.notesappretrofit.presentation.home.viewModel.HomeViewModel
import com.example.notesappretrofit.presentation.home.viewModel.UiState
import com.example.notesappretrofit.presentation.navigation.AuthViewModel
import com.example.notesappretrofit.presentation.navigation.Screen
import com.example.notesappretrofit.ui.theme.backgroungGray
import com.example.notesappretrofit.ui.theme.customfont
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun HomeView(
    viewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val bg = R.color.background
    val background_color : Color = colorResource(id = bg)
    val text_color = colorResource(id = R.color.white)

    val uistate by viewModel.uiState.collectAsState()


    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
    }

    when (uistate) {
        is UiState.Loading -> AnimatedShimmer()
        is UiState.NoInternet -> ConnectionLostScreen()
        is UiState.ServerError -> ServerErrorScreen()
        is UiState.Error -> {
            // Show an error screen with a message if there's an Error state

        }
        is UiState.Initial -> {
            // Handle the initial state or any other state if needed
            Text(text = "hi")
        }
        else -> {
            // Default fallback screen
            Text(text = "Default Screen")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {

}