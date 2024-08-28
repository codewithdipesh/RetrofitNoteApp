package com.example.notesappretrofit.presentation.add_edit.elements

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.example.notesappretrofit.R
import com.example.notesappretrofit.ui.theme.customfont

@Composable
fun CustomTextField(
    label:String,
    value:String,
    onValueChanged : (String)-> Unit,
    maxline :Int,
    fontWeight: FontWeight,
    fontSize: TextUnit,
    context: Context,
    modifier: Modifier = Modifier
) {
    val maxwords = 18*maxline

    Box(modifier = Modifier.fillMaxWidth()) {
        if (value.isEmpty() ) {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = customfont,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    color = colorResource(id = R.color.search_button_color)
                ),
                modifier = modifier.fillMaxWidth()
            )
        }
        BasicTextField(
            value = value,
            onValueChange = {//TODO allow users only to write upto 3 maxline
                if(it.length <= maxwords ){
                    onValueChanged(it)
                }else{
                    Toast.makeText(context,"Length of the title can't be more than ${maxwords}",Toast.LENGTH_SHORT).show()
                }
            },
            modifier = modifier.fillMaxWidth(),
            maxLines = maxline,
            textStyle = TextStyle(
                fontFamily = customfont,
                fontSize = fontSize,
                fontWeight = fontWeight,
                color = Color.White,
            )
        )
    }


}