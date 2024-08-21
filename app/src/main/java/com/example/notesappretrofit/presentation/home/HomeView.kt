package com.example.notesappretrofit.presentation.home
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.notesappretrofit.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.notesappretrofit.presentation.home.elements.AnimatedShimmer
import com.example.notesappretrofit.presentation.home.elements.ConnectionLostScreen
import com.example.notesappretrofit.presentation.home.elements.EmptyNotesUI
import com.example.notesappretrofit.presentation.home.elements.NoteCard
import com.example.notesappretrofit.presentation.home.elements.SearchBar
import com.example.notesappretrofit.presentation.home.elements.ServerErrorScreen
import com.example.notesappretrofit.presentation.home.elements.TopBar
import com.example.notesappretrofit.presentation.home.viewModel.HomeViewModel
import com.example.notesappretrofit.presentation.home.viewModel.UiState
import com.example.notesappretrofit.presentation.navigation.AuthViewModel
import com.example.notesappretrofit.presentation.navigation.Screen
import com.example.notesappretrofit.ui.theme.backgroungGray
import com.example.notesappretrofit.ui.theme.customfont
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale


@Composable
fun HomeView(
    viewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
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
            HomeScreen(viewModel = viewModel)
        }
    }
}


@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {

    val background_color: Color = colorResource(id = R.color.background)
    val text_color = colorResource(id = R.color.white)
    val notes by viewModel.notes.collectAsState()
    val username by viewModel.username.collectAsState()

    Scaffold(containerColor = background_color) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar( viewModel = viewModel, text_color = text_color)

            Spacer(modifier = Modifier.height(30.dp))
            SearchBar(
                onSearch = {
                    viewModel.updateSearchValue(it)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            if(notes.isEmpty()){
                EmptyNotesUI()
            }else{
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalItemSpacing = 16.dp,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ){
                    items(notes){
                            note->
                        NoteCard(note = note)
                    }
                }
            }


        }
    }

}



