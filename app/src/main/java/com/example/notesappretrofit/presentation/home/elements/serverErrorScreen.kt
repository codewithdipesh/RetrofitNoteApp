package com.example.notesappretrofit.presentation.home.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesappretrofit.R
import com.example.notesappretrofit.ui.theme.backgroungGray
import com.example.notesappretrofit.ui.theme.customfont

@Composable
fun ServerErrorScreen() {
    Scaffold(
        containerColor = backgroungGray
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.server_error),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                )
                Column(modifier = Modifier.padding(horizontal = 50.dp, vertical = 70.dp)) {

                    Text(
                        text = "SORRY",
                        fontSize = 40.sp,
                        fontFamily = customfont,
                        color = Color.White
                    )

                    Text(
                        text = "IT'S NOT YOU",
                        fontSize = 40.sp,
                        fontFamily = customfont,
                        color = Color.White
                    )

                    Text(
                        text = "IT'S US",
                        fontSize = 40.sp,
                        fontFamily = customfont,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "We have some trouble with",
                        fontSize = 24.sp,
                        fontFamily = customfont,
                        color = colorResource(id = R.color.darkgray)
                    )
                    Text(
                        text = "the data.Please try to ",
                        fontSize = 24.sp,
                        fontFamily = customfont,
                        color = colorResource(id = R.color.darkgray)
                    )
                    Text(
                        text = "connect later",
                        fontSize = 24.sp,
                        fontFamily = customfont,
                        color = colorResource(id = R.color.darkgray)
                    )

                }
            }
        }
    }
}