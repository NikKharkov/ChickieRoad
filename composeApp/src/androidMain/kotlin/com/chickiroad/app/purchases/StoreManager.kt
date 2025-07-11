package com.chickiroad.app.purchases

import com.chickiroad.app.purchases.model.PurchaseResult
import com.chickiroad.app.purchases.model.RestoreResult
import com.chickiroad.app.purchases.model.StoreProduct

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.resume

actual class StoreManager actual constructor(): KoinComponent {

    private val context: Context by inject()

    private var billingClient: BillingClient? = null
    private val productDetailsMap = mutableMapOf<String, ProductDetails>()

    actual suspend fun initialize() {
        suspendCancellableCoroutine { continuation ->
            billingClient = BillingClient.newBuilder(context)
                .setListener { billingResult, purchases ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        purchases?.forEach { purchase ->
                            if (!purchase.isAcknowledged) {
                                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.purchaseToken)
                                    .build()
                                billingClient?.acknowledgePurchase(acknowledgePurchaseParams) { }
                            }
                        }
                    }
                }
                .enablePendingPurchases()
                .build()

            billingClient?.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resume(Unit)
                    }
                }

                override fun onBillingServiceDisconnected() {

                }
            })
        }
    }

    actual suspend fun getProducts(productIds: List<String>): List<StoreProduct> {
        val client = billingClient
            ?: throw IllegalStateException("Billing client not initialized")

        return suspendCancellableCoroutine { continuation ->
            val productList = productIds.map { productId ->
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(productId)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            }

            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build()

            client.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        val storeProducts = productDetailsList.map { productDetails ->
                            productDetailsMap[productDetails.productId] = productDetails

                            StoreProduct(
                                id = productDetails.productId,
                                title = productDetails.title,
                                description = productDetails.description,
                                price = productDetails.oneTimePurchaseOfferDetails?.formattedPrice ?: "N/A",
                                priceAmountMicros = productDetails.oneTimePurchaseOfferDetails?.priceAmountMicros ?: 0
                            )
                        }
                        continuation.resume(storeProducts)
                    }
                    else -> {
                        continuation.resume(emptyList())
                    }
                }
            }
        }
    }

    actual suspend fun purchase(productId: String): PurchaseResult {
        val client = billingClient
            ?: return PurchaseResult.Error("Billing client not initialized")

        val productDetails = productDetailsMap[productId]
            ?: return PurchaseResult.Error("Product not found. Call getProducts() first")

        val activity = context as? Activity
            ?: return PurchaseResult.Error("Context is not an Activity")

        return suspendCancellableCoroutine { continuation ->
            val productDetailsParamsList = listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .build()
            )

            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()

            val billingResult = client.launchBillingFlow(activity, billingFlowParams)

            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    continuation.resume(PurchaseResult.Success)
                }
                BillingClient.BillingResponseCode.USER_CANCELED -> {
                    continuation.resume(PurchaseResult.UserCancelled)
                }
                BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                    continuation.resume(PurchaseResult.AlreadyPurchased)
                }
                else -> {
                    continuation.resume(PurchaseResult.Error("Purchase failed: ${billingResult.debugMessage}"))
                }
            }
        }
    }

    actual suspend fun restorePurchases(): RestoreResult {
        val client = billingClient
            ?: return RestoreResult.Error("Billing client not initialized")

        return suspendCancellableCoroutine { continuation ->
            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()

            client.queryPurchasesAsync(params) { billingResult, purchasesList ->
                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        val restoredIds = purchasesList
                            .filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }
                            .flatMap { it.products }

                        continuation.resume(RestoreResult.Success(restoredIds))
                    }
                    else -> {
                        continuation.resume(RestoreResult.Error("Failed to restore purchases: ${billingResult.debugMessage}"))
                    }
                }
            }
        }
    }
}