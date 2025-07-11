package com.chickiroad.app.shop.model

data class ShopUiState(
    val bonuses: List<ShopBonus> = emptyList(),
    val isLoading: Boolean = false,
    val purchaseInProgress: String? = null,
    val errorMessage: String? = null,
    val isRestoring: Boolean = false
)