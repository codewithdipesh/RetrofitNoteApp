package com.example.notesappretrofit.presentation.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.notesappretrofit.ui.theme.backgroungGray

@Composable
fun HomeView() {

    Scaffold(
        containerColor = backgroungGray
    ) {
        Text(text = "aUTHORIZED",modifier =Modifier.padding(it), color = Color.White)
    }

}