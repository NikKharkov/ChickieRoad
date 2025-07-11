package com.chickiroad.app.game.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chickieroad.composeapp.generated.resources.Res
import chickieroad.composeapp.generated.resources.bonus1
import chickieroad.composeapp.generated.resources.bonus2
import chickieroad.composeapp.generated.resources.feather
import com.chickiroad.app.game.model.Bonus
import com.chickiroad.app.game.model.BonusType
import org.jetbrains.compose.resources.painterResource

@Composable
fun BonusItem(
    bonus: Bonus,
    modifier: Modifier = Modifier
) {
    val resource = when (bonus.type) {
        BonusType.SMALL -> Res.drawable.bonus1
        BonusType.MEDIUM -> Res.drawable.bonus2
        BonusType.SPEED -> Res.drawable.feather
    }

    Image(
        painter = painterResource(resource),
        contentDescription = "Bonus",
        modifier = modifier
            .size(bonus.size.dp)
            .offset(x = bonus.x.dp, y = bonus.y.dp)
    )
}
