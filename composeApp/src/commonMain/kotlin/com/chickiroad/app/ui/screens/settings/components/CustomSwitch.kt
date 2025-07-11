package com.chickiroad.app.ui.screens.settings.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import chickieroad.composeapp.generated.resources.Res
import chickieroad.composeapp.generated.resources.circle_off
import chickieroad.composeapp.generated.resources.circle_on
import chickieroad.composeapp.generated.resources.off
import chickieroad.composeapp.generated.resources.on
import com.chickiroad.app.ui.theme.Brown
import com.chickiroad.app.ui.theme.DarkOrange
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomSwitch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChanged: (Boolean) -> Unit,
) {
    val switchWidth = 100.dp
    val switchHeight = 44.dp
    val handleSize = 40.dp
    val handlePadding = 2.dp

    val valueToOffset = if (checked) 1f else 0f
    val offset = remember { Animatable(valueToOffset) }
    val scope = rememberCoroutineScope()

    DisposableEffect(checked) {
        if (offset.targetValue != valueToOffset) {
            scope.launch {
                offset.animateTo(valueToOffset, animationSpec = tween(1000))
            }
        }
        onDispose { }
    }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .width(switchWidth)
            .height(switchHeight)
            .clip(RoundedCornerShape(switchHeight))
            .background(DarkOrange)
            .border(3.dp, Brown, RoundedCornerShape(switchHeight))
            .toggleable(
                value = checked,
                onValueChange = onCheckedChanged,
                role = Role.Switch,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
    ) {
        Image(
            painter = painterResource(if (checked) Res.drawable.on else Res.drawable.off),
            contentDescription = null,
            modifier = Modifier
                .size(handleSize)
                .offset(x = (switchWidth - handleSize - handlePadding * 2f) * (1f - offset.value))
                .padding(horizontal = 8.dp)
        )

        Box(
            modifier = Modifier
                .padding(horizontal = handlePadding)
                .size(handleSize)
                .offset(x = (switchWidth - handleSize - handlePadding * 2f) * offset.value)
                .clip(CircleShape)
                .background(Color.Transparent)
        ) {
            Image(
                painter = painterResource(if (checked) Res.drawable.circle_on else Res.drawable.circle_off),
                contentDescription = null,
                modifier = Modifier
                    .size(handleSize)
            )
        }
    }
}