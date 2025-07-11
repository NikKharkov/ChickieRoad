package com.chickiroad.app.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

@Composable
fun TextWithBorder(
    text: String,
    fontSize: TextUnit,
    textColor: Color,
    borderColor: Color
) {
    Box {
        Text(
            text = text,
            style = TextStyle(
                fontSize = fontSize,
                color = borderColor,
                textAlign = TextAlign.Center,
                drawStyle = Stroke(
                    width = 12f,
                    join = StrokeJoin.Round
                )
            )
        )
        Text(
            text = text,
            style = TextStyle(
                fontSize = fontSize,
                color = textColor,
                textAlign = TextAlign.Center
            )
        )
    }
}