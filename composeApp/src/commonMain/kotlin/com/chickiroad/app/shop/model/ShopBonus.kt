package com.chickiroad.app.shop.model

import org.jetbrains.compose.resources.DrawableResource

data class ShopBonus(
    val id: String,
    val name: String,
    val description: String,
    val price: String,
    val iconRes: DrawableResource,
    val isPurchased: Boolean
)