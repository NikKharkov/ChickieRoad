package com.chickiroad.app.purchases

import com.chickiroad.app.managers.AppSettings
import com.chickiroad.app.purchases.model.*

class StoreRepository(
    private val storeManager: StoreManager,
    private val appSettings: AppSettings
) {
    companion object {
        const val PRODUCT_DOUBLE_X2 = "double_x2"
        const val PRODUCT_HEALTH_BOOST = "health_boost"
        const val PRODUCT_SPEED_X3 = "speed_x3"
        const val PRODUCT_DOUBLE_X5 = "double_x5"

        val ALL_PRODUCT_IDS = listOf(
            PRODUCT_DOUBLE_X2,
            PRODUCT_HEALTH_BOOST,
            PRODUCT_SPEED_X3,
            PRODUCT_DOUBLE_X5
        )
    }

    suspend fun initialize() {
        storeManager.initialize()
    }

    suspend fun getStoreProducts(): List<StoreProduct> {
        return storeManager.getProducts(ALL_PRODUCT_IDS)
    }

    suspend fun purchaseProduct(productId: String): PurchaseResult {
        val result = storeManager.purchase(productId)

        if (result is PurchaseResult.Success) {
            savePurchaseToSettings(productId)
        }

        return result
    }

    suspend fun restorePurchases(): RestoreResult {
        val result = storeManager.restorePurchases()

        if (result is RestoreResult.Success) {
            result.restoredProductIds.forEach { productId ->
                savePurchaseToSettings(productId)
            }
        }

        return result
    }

    fun isPurchased(productId: String): Boolean {
        return when (productId) {
            PRODUCT_DOUBLE_X2 -> appSettings.doubleX2Purchased
            PRODUCT_HEALTH_BOOST -> appSettings.healthBoostPurchased
            PRODUCT_SPEED_X3 -> appSettings.speedX3Purchased
            PRODUCT_DOUBLE_X5 -> appSettings.doubleX5Purchased
            else -> false
        }
    }

    private fun savePurchaseToSettings(productId: String) {
        when (productId) {
            PRODUCT_DOUBLE_X2 -> appSettings.doubleX2Purchased = true
            PRODUCT_HEALTH_BOOST -> appSettings.healthBoostPurchased = true
            PRODUCT_SPEED_X3 -> appSettings.speedX3Purchased = true
            PRODUCT_DOUBLE_X5 -> appSettings.doubleX5Purchased = true
        }
    }
}