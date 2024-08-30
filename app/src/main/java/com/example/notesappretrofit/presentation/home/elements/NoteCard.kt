package com.example.notesappretrofit.presentation.home.elements
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.Image
import com.example.notesappretrofit.R

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.notesappretrofit.data.remote.note.dto.NoteData
import com.example.notesappretrofit.presentation.home.elements.BiometricPromptManager.*
import com.example.notesappretrofit.presentation.navigation.Screen
import com.example.notesappretrofit.ui.theme.customfont
import com.example.notesappretrofit.utils.getDatefromString
import com.skydoves.cloudy.cloudy
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun NoteCard(
    biometricSelectedId:MutableState<Int?>,
    biometricEnabled:MutableState<Boolean>,
    note: NoteData,
    navController: NavController,
    onDelete:(Int)->Unit,
    graphicsLayer:GraphicsLayer,
    promptManager: BiometricPromptManager,
) {
    var showOptions by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()



    Box(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(28.dp))
        .pointerInput(Unit) {
            detectTapGestures(
                onLongPress = {
                    if (!note.isLocked) {
                        scope.launch {
                            showOptions = true
                            delay(3000)
                            showOptions = false
                        }

                    }
                },
                onTap = {
                    if (note.isLocked) {
                        biometricSelectedId.value = note.id
                        Log.d("tap", biometricSelectedId.toString())
                        //locked note -> biometric->open
                        biometricEnabled.value = true
                        promptManager.showBiometricPrompt(
                            title = "Authenticate",
                            description = "Please authenticate to access the note"
                        )

                    } else {
                        navController.navigate(Screen.AddorEdit.route + "/${note.id}")
                    }

                }
            )
        }
    ){
    //note details
        Box(modifier = Modifier
            .fillMaxWidth()
            .then(
                if (note.isLocked) {
                    Modifier.cloudy(radius = 30, graphicsLayer = graphicsLayer)
                } else {
                    Modifier
                }
            )
            .background(colorResource(id = R.color.note_bg))
        ){

            if(showOptions){
                IconButton(onClick = {
                    onDelete(note.id)
                },
                modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(imageVector = Icons.Default.Delete,
                        contentDescription = "delete",
                        tint = Color.Red
                    )
                }
            }

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
        //locked icon and text
        if(note.isLocked){

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(bottom = 8.dp), //for centre in the box
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.locked_icon),
                        contentDescription ="locked" )
                    Text(
                        text = "Locked Note",
                        fontFamily = customfont,
                        fontSize = 20.sp,
                        color = Color.White,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Gray, // Shadow color
                                offset = Offset(4f, 4f), // Shadow offset (x, y)
                                blurRadius = 8f // Shadow blur radius
                            )
                        )
                    )

            }

        }


    }

}
