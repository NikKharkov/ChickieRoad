package com.chickiroad.app.purchases.model

data class StoreProduct(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val priceAmountMicros: Long = 0
)