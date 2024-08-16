package com.example.notesappretrofit.presentation.register_login.elements

import android.util.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(
    text :String,
    bg :Color = Color.White,
    contentColor :Color =Color.Black,
    height : Dp = 42.dp,
    width : Dp = 150.dp,
    onClick:()->Unit
) {

    Box(
        modifier = Modifier
            .height(height)
            .width(width)
            .clip(CustomButtonShape())
            .background(bg)
            .clickable {
                       onClick()
            },
        contentAlignment = Alignment.Center

    ){
        Text(
            text = text,
            fontSize = 18.sp,
            color = contentColor
        )
    }

}