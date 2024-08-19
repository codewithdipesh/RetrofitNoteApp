package com.example.notesappretrofit.presentation.home.elements

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.notesappretrofit.ui.theme.backgroungGray

@Composable
fun AnimatedShimmer() {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x= translateAnim.value,y=translateAnim.value)
    )
    LoadingScreen(brush = brush)
}

@Composable
fun LoadingScreen(
    brush: Brush
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroungGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(
                modifier = Modifier
                    .height(42.dp)
                    .fillMaxWidth(0.6f)
                    .clip(RoundedCornerShape(25.dp))
                    .background(brush)

            )
            Spacer(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(brush)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Spacer(
            modifier = Modifier
                .height(38.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .background(brush)

        )
        Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalStaggeredGrid(
                   columns = StaggeredGridCells.Fixed(2),
                modifier =Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                items(emptyNoteList.list) {
                    Box(
                        modifier = Modifier
                            .height(it.height)
                            .weight(1f)
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .background(brush)
                    )
                }
            }

    }

}
data class emptyElement(
    val height : Dp
)
object emptyNoteList{
    val list = listOf<emptyElement>(
        emptyElement(250.dp),
        emptyElement(150.dp),
        emptyElement(250.dp),
        emptyElement(250.dp),
        emptyElement(250.dp),
        emptyElement(150.dp)
    )
}

@Preview
@Composable
fun LoadingScreenPreview(){
    AnimatedShimmer()

}