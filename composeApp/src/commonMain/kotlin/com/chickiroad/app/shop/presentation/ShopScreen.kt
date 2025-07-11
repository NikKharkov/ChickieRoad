package com.chickiroad.app.shop.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chickieroad.composeapp.generated.resources.Res
import chickieroad.composeapp.generated.resources.background_game_over
import chickieroad.composeapp.generated.resources.btn_buy
import chickieroad.composeapp.generated.resources.btn_home
import chickieroad.composeapp.generated.resources.btn_purchased
import chickieroad.composeapp.generated.resources.shop_logo
import com.chickiroad.app.shop.model.ShopBonus
import com.chickiroad.app.ui.shared.Background
import com.chickiroad.app.ui.shared.CustomButton
import com.chickiroad.app.ui.shared.TextWithBorder
import com.chickiroad.app.ui.theme.Cyan
import com.chickiroad.app.ui.theme.DarkBlue
import com.chickiroad.app.ui.theme.DarkYellow
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShopScreen(
    viewModel: ShopViewModel = koinViewModel(),
    onHomeClick: () -> Unit
) {
    val uiState by viewModel.shopUiState.collectAsState()

    Background(backgroundResImage = Res.drawable.background_game_over)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.shop_logo),
            contentDescription = "Shop",
            modifier = Modifier
                .width(160.dp)
                .height(90.dp)
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = DarkYellow,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(48.dp)
                    )
                    TextWithBorder(
                        text = "Loading products...",
                        fontSize = 16.sp,
                        textColor = Color.White,
                        borderColor = DarkBlue
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(uiState.bonuses) { bonus ->
                    ShopBonusCard(
                        bonus = bonus,
                        isPurchaseInProgress = uiState.purchaseInProgress == bonus.id,
                        onPurchase = { viewModel.purchaseBonus(bonus.id) }
                    )
                }
            }
        }

        uiState.errorMessage?.let { error ->

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextWithBorder(
                    text = error,
                    fontSize = 14.sp,
                    textColor = Color.White,
                    borderColor = DarkBlue
                )

                CustomButton(
                    image = painterResource(Res.drawable.btn_home),
                    onClick = { viewModel.clearError() },
                    modifier = Modifier
                        .width(60.dp)
                        .height(48.dp)
                )
            }
        }

        CustomButton(
            image = painterResource(Res.drawable.btn_home),
            onClick = onHomeClick,
            modifier = Modifier
                .width(80.dp)
                .height(64.dp)
        )
    }
}

@Composable
fun ShopBonusCard(
    bonus: ShopBonus,
    isPurchaseInProgress: Boolean,
    onPurchase: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .let { mod ->
                if (isPurchaseInProgress) {
                    mod.alpha(0.7f)
                } else mod
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box {
            Image(
                painter = painterResource(bonus.iconRes),
                contentDescription = bonus.name,
                modifier = Modifier.size(70.dp)
            )

            if (isPurchaseInProgress) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(DarkBlue.copy(alpha = 0.7f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = DarkYellow,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

        TextWithBorder(
            text = bonus.name,
            fontSize = 14.sp,
            textColor = Color.White,
            borderColor = Color.Black
        )

        TextWithBorder(
            text = bonus.price,
            fontSize = 48.sp,
            textColor = DarkBlue,
            borderColor = Cyan
        )

        val buttonImage = when {
            bonus.isPurchased -> painterResource(Res.drawable.btn_purchased)
            isPurchaseInProgress -> painterResource(Res.drawable.btn_buy)
            else -> painterResource(Res.drawable.btn_buy)
        }

        CustomButton(
            image = buttonImage,
            onClick = {
                if (!bonus.isPurchased && !isPurchaseInProgress) {
                    onPurchase()
                }
            },
            modifier = Modifier
                .width(80.dp)
                .height(64.dp)
        )
    }
}