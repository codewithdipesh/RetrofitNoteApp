package com.example.notesappretrofit.presentation.register_login.elements
import com.example.notesappretrofit.R

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesappretrofit.ui.theme.NotesAppRetrofitTheme
import com.example.notesappretrofit.ui.theme.customfont
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale


@Composable
fun FormFieldWithIcon(
    name : String,
    onChange : (String)-> Unit,
    color : Color,
    isEnabled : Boolean = true
) {
    var value by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = name,
            color = color,
            fontSize = 18.sp
            )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value ,
            onValueChange = {
                 value = it
                onChange(it)
            },
            enabled = isEnabled,
            placeholder = {
                Text(
                    text = "Enter ${name.lowercase(Locale.ROOT)} ...",
                    color = color,
                    fontSize = 16.sp
                    )
            },
            visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                           val image = if(passwordVisible)
                               R.drawable.baseline_visibility_24
                           else R.drawable.baseline_visibility_off_24
                IconButton(onClick = {
                   scope.launch {
                        passwordVisible = true
                        delay(800)
                        passwordVisible = false
                    }
                }) {
                  Icon(painter = painterResource(id = image), contentDescription = null)
                }

            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
            ),
            textStyle = TextStyle(color = color, fontFamily = customfont)
            )
    }
}


