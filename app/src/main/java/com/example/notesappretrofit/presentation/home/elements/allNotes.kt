package com.example.notesappretrofit.presentation.home.elements

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notesappretrofit.presentation.home.viewModel.HomeViewModel
import com.example.notesappretrofit.presentation.navigation.Screen
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AllNotes(
    biometricSelectedId:MutableState<Int?>,
    biometricEnabled:MutableState<Boolean>,
    viewModel : HomeViewModel,
    graphicsLayer:GraphicsLayer,
    navController: NavController,
    promptManager:BiometricPromptManager
) {

    val notes by viewModel.notes.collectAsState()

    var isRefreshing by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    SearchBar(
        onSearch = {
            viewModel.searchNote(it)
        }
    )
    Spacer(modifier = Modifier.height(16.dp))
    if (notes.isEmpty()) {
        EmptyNotesUI("You don't have any notes yet")
    } else {
        PullToRefreshLazyColumn(
            items = notes ,
            content = {note->
                      NoteCard(
                          biometricEnabled= biometricEnabled,
                          biometricSelectedId=biometricSelectedId,
                          promptManager=promptManager,
                          note = note,
                          graphicsLayer = graphicsLayer,
                          navController = navController,
                          onDelete = {
                              scope.launch {
                                  viewModel.deleteNote(it.toString())
                              }

                          }

                      )
            },
            isRefreshing = isRefreshing ,
            onRefresh = {
                scope.launch{
                    isRefreshing = true
                    Log.d("refreshing",isRefreshing.toString())
                    viewModel.refreshNotes()
                    Log.d("refreshing",isRefreshing.toString())
                    isRefreshing = false
                    Log.d("refreshing",isRefreshing.toString())

                }
            }
        )
    }
}