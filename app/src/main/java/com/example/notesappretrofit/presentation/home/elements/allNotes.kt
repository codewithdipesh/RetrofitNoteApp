package com.example.notesappretrofit.presentation.home.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.unit.dp
import com.example.notesappretrofit.presentation.home.viewModel.HomeViewModel

@Composable
fun AllNotes(
    viewModel : HomeViewModel,
    graphicsLayer:GraphicsLayer
) {

    val notes by viewModel.notes.collectAsState()



    SearchBar(
        onSearch = {
            viewModel.searchNote(it)
        }
    )
    Spacer(modifier = Modifier.height(16.dp))
    if (notes.isEmpty()) {
        EmptyNotesUI("You don't have any notes yet")
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(notes) { note ->
                NoteCard(note = note,graphicsLayer = graphicsLayer)
            }
        }
    }
}