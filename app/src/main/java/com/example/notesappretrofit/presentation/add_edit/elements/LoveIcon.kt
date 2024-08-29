package com.example.notesappretrofit.presentation.add_edit.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.notesappretrofit.presentation.add_edit.viewmodel.NoteUi
import com.example.notesappretrofit.presentation.home.viewModel.UiState

@Composable
fun LoveIcon(
    state :NoteUi ,
    onClick : ()-> Unit = {}
){
    var showLove by remember(state.isFavorite) {
        mutableStateOf(state.isFavorite)
    }
    var color : Color = if(showLove) Color.Red else Color.White

    IconButton(onClick = {
        onClick()
        showLove = !showLove
    }) {
        Icon(
            imageVector = Icons.Default.Favorite ,
            contentDescription = null,
            tint = color
        )
    }

}
