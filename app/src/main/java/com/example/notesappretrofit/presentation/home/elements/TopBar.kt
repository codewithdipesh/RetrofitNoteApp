package com.example.notesappretrofit.presentation.home.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesappretrofit.R
import com.example.notesappretrofit.presentation.home.viewModel.HomeViewModel
import com.example.notesappretrofit.ui.theme.customfont

@Composable
fun TopBar (
    text_color:Color,
    viewModel:HomeViewModel
){

    val greeting by viewModel.greeting.collectAsState()
    val username by viewModel.username.collectAsState()

    Row (modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = "Good ${greeting}",
            fontSize = 36.sp,
            fontFamily = customfont,
            color = text_color
        )
        Box(modifier = Modifier
            .size(45.dp)
            .clip(CircleShape)
            .background(colorResource(id = R.color.pfpcolor))
        ){
            if(username.isNotEmpty()){
                Text(text = "${username[0]}",
                    color = text_color,
                    fontSize = 40.sp,
                    modifier = Modifier.align(Alignment.Center),
                    fontFamily = customfont
                )
            }

        }

    }
}