package com.example.notesappretrofit.presentation.home.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesappretrofit.R
import com.example.notesappretrofit.ui.theme.customfont

@Composable
fun SearchBar(
    text_color: Color = Color.White,
    onSearch:(String)->Unit
) {
    var searchValue by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .height(42.dp)
            .background(colorResource(id = R.color.search_button_color))
    ){

        if(searchValue.isEmpty()){
            Row(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.search_icon), contentDescription = null)
                Text(
                    text = "Search notes...",
                    color = text_color,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(start = 14.dp)
                )
            }

        }

        BasicTextField(
            value = searchValue,
            onValueChange = {
                searchValue =it
               onSearch(it)
            },
            singleLine = true,
            textStyle = TextStyle(color = text_color, fontFamily = customfont),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .align(Alignment.CenterStart)
        )

    }


}