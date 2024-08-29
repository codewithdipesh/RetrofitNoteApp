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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import com.example.notesappretrofit.R

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.notesappretrofit.presentation.add_edit.elements.LoveIcon
import com.example.notesappretrofit.presentation.add_edit.viewmodel.AddEditViewModel
import com.example.notesappretrofit.utils.getCurrentDate
import com.example.notesappretrofit.utils.getDatefromString
import kotlinx.coroutines.launch

@Composable
fun AddEditScreen(
    id : Int,
    viewModel : AddEditViewModel,
    navController: NavController
) {

    val state  by viewModel.UiState.collectAsState()
    val scope = rememberCoroutineScope()

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
                                    viewModel.updateNoteDetails(id.toString())
                                    navController.navigateUp()
                                }
                            }else{
                                if(state.title.isEmpty() || state.description.isEmpty()){
                                    //do nothing
                                }else{
                                    viewModel.createNote()
                                }
                                navController.navigateUp()
                            }
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
        }
    ){
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