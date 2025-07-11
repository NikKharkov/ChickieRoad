package com.chickiroad.app.purchases.model

sealed class PurchaseResult {
    data object Success : PurchaseResult()
    data object UserCancelled : PurchaseResult()
    data object AlreadyPurchased : PurchaseResult()
    data class Error(val message: String) : PurchaseResult()
}