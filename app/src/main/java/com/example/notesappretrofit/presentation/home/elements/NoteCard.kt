package com.example.notesappretrofit.presentation.home.elements
import com.example.notesappretrofit.R

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesappretrofit.data.remote.note.dto.NoteData
import com.example.notesappretrofit.ui.theme.customfont
import com.example.notesappretrofit.utils.getDatefromString

@Composable
fun NoteCard(
    note: NoteData,
    onClick :()-> Unit = {}
) {.
    Box(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(28.dp))
        .background(colorResource(id = R.color.note_bg))
        .clickable {
            onClick()
        }
    ){
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
           Text(
              text = note.title,
              fontFamily = customfont,
               fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
               maxLines = 1,
              color = Color.White
           )
            Text(
                text = note.description,
                fontFamily = customfont,
                fontSize = 14.sp,
                color = Color.White,
                maxLines = 9,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = getDatefromString(note.createdAt),
                fontFamily = customfont,
                fontSize = 12.sp,
                color =  colorResource(id = R.color.note_desc)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotecardPreview() {
    NoteCard(NoteData("2024-05-12","hi",1,"Purpose of life"))
}