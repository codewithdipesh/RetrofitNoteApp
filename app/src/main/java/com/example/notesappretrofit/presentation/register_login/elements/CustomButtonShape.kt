package com.example.notesappretrofit.presentation.register_login.elements

import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class CustomButtonShape(): Shape{
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    left = size.width * 0.12f,
                    top = 0f,
                    right = size.width - size.width * .12f,
                    bottom = size.height
                )
            )
            moveTo(0f,0f)
            lineTo(size.width * 0.12f,0f)
            lineTo(size.width * 0.12f,size.height)
            lineTo(0f,size.height - size.height*0.45f)
            close()

            moveTo(size.width -size.width*0.12f,0f)
            lineTo(size.width,size.height *0.45f)
            lineTo(size.width,size.height)
            lineTo(size.width - size.width*0.12f,size.height)
            close()

        }
        return Outline.Generic(path)
    }

}