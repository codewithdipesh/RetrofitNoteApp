package com.example.notesappretrofit.presentation.home.elements

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notesappretrofit.R
import com.example.notesappretrofit.presentation.home.viewModel.HomeViewModel
import com.example.notesappretrofit.presentation.navigation.Screen


@Composable
fun HomeView(
    viewModel: HomeViewModel,
    navController: NavController,
    graphicsLayer: GraphicsLayer
) {

    val background_color: Color = colorResource(id = R.color.background)
    val text_color = colorResource(id = R.color.white)

    var subScreen by remember {
        mutableStateOf(HomeScreen.ALLNOTES)
    }

    Scaffold(
        containerColor = background_color,
        bottomBar = {
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .height(130.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(67.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White)
                        .clickable {
                            navController.navigate(Screen.AddorEdit.route+"/0")
                        },
                    contentAlignment = Alignment.Center
                ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "add",
                            modifier = Modifier.size(20.dp)
                        )

                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .height(70.dp)
                        .paint(
                            painter = painterResource(R.drawable.bottom_nav),
                            contentScale = ContentScale.FillHeight
                        )
                ) {
                    //homescreen
                    if(subScreen == HomeScreen.ALLNOTES){

                        Image(
                            painter = painterResource(id = R.drawable.dashborad_filled),
                            contentDescription = "home",
                            modifier = Modifier.clickable {
                                subScreen = HomeScreen.ALLNOTES
                            }
                            )
                    }else{
                        Image(
                            painter = painterResource(id = R.drawable.dashborad),
                            contentDescription = "home",
                            modifier = Modifier.clickable {
                                subScreen = HomeScreen.ALLNOTES
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(22.dp))
                    //favorites
                    if(subScreen == HomeScreen.FAVORITES){
                        Image(
                            painter = painterResource(id = R.drawable.star_filled),
                            contentDescription = "favorites",
                            modifier = Modifier.clickable {
                                subScreen = HomeScreen.FAVORITES
                            }
                        )
                    }else{
                        Image(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = "favorites",
                            modifier = Modifier.clickable {
                                subScreen = HomeScreen.FAVORITES
                            }
                        )
                    }
                }
            }
        }
    ) {
        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(viewModel = viewModel, text_color = text_color)

            Spacer(modifier = Modifier.height(30.dp))
            if(subScreen == HomeScreen.ALLNOTES){
                AllNotes(viewModel = viewModel,graphicsLayer = graphicsLayer, navController = navController)
            }else{
                FavoriteNotes(viewModel = viewModel,graphicsLayer = graphicsLayer,navController = navController)
            }



        }

    }
}

enum class HomeScreen{
    ALLNOTES,
    FAVORITES
}