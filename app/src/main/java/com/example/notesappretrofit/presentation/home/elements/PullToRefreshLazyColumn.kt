package com.example.notesappretrofit.presentation.home.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.notesappretrofit.data.remote.note.dto.NoteDto
import com.example.notesappretrofit.domain.entity.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  PullToRefreshLazyColumn(
    items : List<Note>,
    content: @Composable (Note)-> Unit,
    isRefreshing : Boolean,
    onRefresh:()-> Unit,
    modifier:Modifier = Modifier
) {
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(isRefreshing){
        if(isRefreshing){
            pullToRefreshState.startRefresh()
        }else{
            pullToRefreshState.endRefresh()
        }
    }


    Box(
        modifier= modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ){
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(items) { note ->
               content(note)
            }
        }
        if(pullToRefreshState.isRefreshing){
            LaunchedEffect(true){
                onRefresh()
            }
        }


        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter),
            containerColor = Color.Transparent,
            contentColor = Color.White
        )
    }
}