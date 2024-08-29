package com.example.notesappretrofit.presentation.add_edit.elements
import com.example.notesappretrofit.R

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun DropDownMenuOptions(
    locked :Boolean,
    expanded : Boolean,
    onDismissRequest :() -> Unit,
    toggleLockStatus:()->Unit,
    modifier: Modifier = Modifier
){
    DropdownMenu(
        expanded = expanded ,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.background(Color.White))
    {
        DropdownMenuItem(
            text = {
                if(locked){
                    DropDownOption(text = "Unlock Note" , icon = painterResource(id = R.drawable.baseline_lock_open_24))
                }else{
                    DropDownOption(text = "Lock Note" , icon = painterResource(id = R.drawable.baseline_lock_outline_24))
                }

            },
            onClick = {
                toggleLockStatus()
            }
        )


    }
}



@Composable
fun DropDownOption(
    text : String,
    color : Color = Color.Black,
    icon : Painter,
    iconColor : Color = Color.Black
){
    Row (modifier = Modifier.padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically){
        Icon(painter = icon ,
            contentDescription = null,
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = color
        )


    }
}