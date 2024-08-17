package com.example.notesappretrofit.presentation.register_login.elements
import com.example.notesappretrofit.R

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesappretrofit.ui.theme.customfont
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun FormFieldWithIcon(
    field: String,
    onChange: (String) -> Unit,
    inputColor: Color = Color.White,
    fieldColor :Color = colorResource(R.color.gray),
    isEnabled: Boolean = true,
    maxWord : Int = 10
) {
    var value by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var showErrorText by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = field, color = fieldColor, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .border(2.dp, fieldColor, RectangleShape)
        ) {
            if (value.isEmpty()) {
                Text(
                    text = "Enter ${field.lowercase(Locale.ROOT)} ...",
                    color = fieldColor,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.CenterStart)
                )
            }
            BasicTextField(
                value = value,
                onValueChange = {
                    if (it.length <= maxWord) {
                        value = it
                        onChange(it)
                        showErrorText = false
                    }else{
                        showErrorText = true
                    }
                },
                enabled = isEnabled,
                singleLine = true,
                textStyle = TextStyle(color = inputColor, fontFamily = customfont),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .align(Alignment.CenterStart)
            )
            IconButton(
                onClick = {
                    scope.launch {
                        passwordVisible = true
                        delay(300)
                        passwordVisible = false
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (passwordVisible) R.drawable.baseline_visibility_24
                        else R.drawable.baseline_visibility_off_24
                    ),
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = inputColor
                )
            }
        }
        if(showErrorText){
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${field.lowercase(Locale.ROOT)}'s length can't be more than ${maxWord}",
                fontSize = 14.sp,
                color = Color.Red
            )
        }
    }
}