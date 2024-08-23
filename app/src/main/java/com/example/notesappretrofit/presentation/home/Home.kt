package com.example.notesappretrofit.presentation.home
import android.widget.Toast
import com.example.notesappretrofit.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notesappretrofit.presentation.home.elements.AnimatedShimmer
import com.example.notesappretrofit.presentation.home.elements.ConnectionLostScreen
import com.example.notesappretrofit.presentation.home.elements.EmptyNotesUI
import com.example.notesappretrofit.presentation.home.elements.HomeView
import com.example.notesappretrofit.presentation.home.elements.NoteCard
import com.example.notesappretrofit.presentation.home.elements.SearchBar
import com.example.notesappretrofit.presentation.home.elements.ServerErrorScreen
import com.example.notesappretrofit.presentation.home.elements.TopBar
import com.example.notesappretrofit.presentation.home.viewModel.HomeViewModel
import com.example.notesappretrofit.presentation.home.viewModel.UiState
import com.example.notesappretrofit.presentation.navigation.AuthViewModel
import com.example.notesappretrofit.presentation.navigation.Screen
import dev.chrisbanes.haze.HazeState
import kotlinx.coroutines.launch


@Composable
fun Home(
    viewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    navController: NavController,

) {
    
    val uistate by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
            HomeView(viewModel = viewModel,navController = navController)
        }
    }
}





