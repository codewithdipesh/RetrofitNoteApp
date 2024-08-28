package com.example.notesappretrofit.presentation.add_edit
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import com.example.notesappretrofit.R

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.notesappretrofit.data.remote.note.dto.NoteData
import com.example.notesappretrofit.presentation.add_edit.elements.CustomTextField
import com.example.notesappretrofit.presentation.add_edit.viewmodel.AddEditViewModel

@Composable
fun AddEditScreen(
    id : Int,
    viewModel : AddEditViewModel,
    navController: NavController
) {

    val state  by viewModel.UiState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit){
        if(id != 0){
            viewModel.fetchNoteDetails(id.toString())
        }else{
            viewModel.updateTitle("")
            viewModel.updateDescription("")
            viewModel.updateFavorite(false)
            viewModel.updateLockStatus(false)
        }
    }


    Scaffold (
        containerColor = colorResource(id = R.color.background)
    ){
        Column(
            modifier = Modifier.
            padding(it)
        ) {
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
                                viewModel.updateNoteDetails(id.toString())
                            }else{
                                viewModel.createNote()
                            }

                            navController.navigateUp()
                        }
                )

                Image(
                    painter = painterResource(id = R.drawable.more_option_button),
                    contentDescription = "more option",
                    modifier = Modifier
                        .clickable {
                            //TODO MORE OPTION
                        }
                )
                
            }

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



        }
    }

}