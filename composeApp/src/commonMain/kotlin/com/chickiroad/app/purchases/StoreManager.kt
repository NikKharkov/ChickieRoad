package com.chickiroad.app.purchases

import com.chickiroad.app.purchases.model.PurchaseResult
import com.chickiroad.app.purchases.model.RestoreResult
import com.chickiroad.app.purchases.model.StoreProduct

expect class StoreManager() {
    suspend fun initialize()
    suspend fun getProducts(productIds: List<String>): List<StoreProduct>
    suspend fun purchase(productId: String): PurchaseResult
    suspend fun restorePurchases(): RestoreResult
}