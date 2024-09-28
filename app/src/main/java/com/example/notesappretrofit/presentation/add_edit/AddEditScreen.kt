package com.example.notesappretrofit.presentation.add_edit
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.example.notesappretrofit.R

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.notesappretrofit.presentation.add_edit.elements.CustomTextField
import com.example.notesappretrofit.presentation.add_edit.elements.DropDownMenuOptions
import com.example.notesappretrofit.presentation.add_edit.elements.LoveIcon
import com.example.notesappretrofit.presentation.add_edit.viewmodel.AddEditViewModel
import com.example.notesappretrofit.utils.getDatefromString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddEditScreen(
    id : Int,
    viewModel : AddEditViewModel,
    navController: NavController
) {

    val state  by viewModel.UiState.collectAsState()
    val scope = rememberCoroutineScope()

    var expanded by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current


    LaunchedEffect(Unit){
        if(id != 0){
            viewModel.fetchNoteDetails(id.toString())
            Log.d("add_edit screen",state.toString())
        }else{
            viewModel.fetchTempCounter()
            viewModel.updateTitle("")
            viewModel.updateDescription("")
            viewModel.updateFavorite(false)
            viewModel.updateLockStatus(false)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }


    Scaffold (
        containerColor = colorResource(id = R.color.background),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.back_navigation) ,
                    contentDescription = "back navigation",
                    modifier = Modifier
                        .clickable {
                            if(id != 0){
                                if(state.title.isEmpty() || state.description.isEmpty()){
                                    scope.launch {
                                        Toast.makeText(context,"Title and description can't be empty",Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    viewModel.updateNoteDetails(id)
                                    navController.navigateUp()
                                }
                            }else{
                                if(state.title.isEmpty() || state.description.isEmpty()){
                                    if(state.title.isEmpty() && state.description.isEmpty()){
                                        //do nothing
                                        navController.navigateUp()

                                    }
                                    else if(state.title.isEmpty()){
                                        scope.launch {
                                            Toast.makeText(context,"Title  can't be empty",Toast.LENGTH_SHORT).show()
                                        }
                                    }else{
                                        scope.launch {
                                            Toast.makeText(context,"Description can't be empty",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }else{
                                    viewModel.createNote()
                                    navController.navigateUp()

                                }

                            }
                        }
                )

                Image(
                    painter = painterResource(id = R.drawable.more_option_button),
                    contentDescription = "more option",
                    modifier = Modifier
                        .clickable {
                            expanded = true
                        }
                )

            }
        }
    ){
        Surface (modifier = Modifier
            .fillMaxSize()
            .padding(it),
            color = colorResource(id = R.color.background)
        ) {
            //Drop Down menu
            Box(
                modifier = Modifier
                    .fillMaxSize() //TODO FIX MENU POSITION
                    .padding(end = 16.dp, top = 16.dp, start = 250.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                DropDownMenuOptions(
                    locked = state.isLocked,
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    toggleLockStatus = {
                        scope.launch {
                            expanded = false
                            delay(300)
                            viewModel.updateLockStatus(!state.isLocked)
                        }

                    }
                )
            }

        }
            //Favorite Icon
            Column(
                modifier = Modifier.
                padding(it)
            ) {

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                    contentAlignment = Alignment.CenterEnd
                ){
                    Log.d("Icon",state.isFavorite.toString())
                    LoveIcon(
                        state = state,
                        onClick = {
                            viewModel.updateFavorite(!state.isFavorite)
                        }
                    )
                }
                //Fields
                CustomTextField(
                    label = "Enter Title",
                    value = state.title,
                    onValueChanged ={
                        viewModel.updateTitle(it)
                    } ,
                    maxline = 3,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp,
                    context = context,
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = getDatefromString(state.createdAt),
                    color = colorResource(id = R.color.date_color),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField(
                    label = "Enter Description",
                    value = state.description,
                    onValueChanged ={
                        viewModel.updateDescription(it)
                    } ,
                    maxline = 100,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    context = context,
                    modifier = Modifier.padding(16.dp)
                )

        }

    }

}