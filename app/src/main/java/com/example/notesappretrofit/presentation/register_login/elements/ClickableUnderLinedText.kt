package com.example.notesappretrofit.presentation.register_login.elements

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit

@Composable
fun ClickableUnderlinedText(
    text: String,
    onClick: () -> Unit,
    color: Color,
    size: TextUnit
) {
    Text(
        text = text,
        color = color,
        fontSize = size ,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable(onClick ={
            onClick()
        })
    )
}