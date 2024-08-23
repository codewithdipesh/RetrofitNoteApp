package com.example.notesappretrofit.presentation.home.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesappretrofit.presentation.home.viewModel.HomeViewModel
import com.example.notesappretrofit.ui.theme.customfont
import dev.chrisbanes.haze.HazeState

@Composable
fun FavoriteNotes(viewModel : HomeViewModel) {

    val favNotes by viewModel.favNotes.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Favorites",
            fontFamily = customfont,
            fontSize = 30.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (favNotes.isEmpty()) {
            EmptyNotesUI("No favorite notes")
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalItemSpacing = 16.dp,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(favNotes) { note ->
                    NoteCard(note = note)
                }
            }
        }
    }

}