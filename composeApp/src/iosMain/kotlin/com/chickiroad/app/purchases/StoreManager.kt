package com.chickiroad.app.purchases

import com.chickiroad.app.purchases.model.PurchaseResult
import com.chickiroad.app.purchases.model.RestoreResult
import com.chickiroad.app.purchases.model.StoreProduct
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.*
import platform.StoreKit.*
import platform.darwin.NSObject
import kotlin.coroutines.resume

actual class StoreManager actual constructor() {

    private var products: List<SKProduct> = emptyList()
    private val productMap = mutableMapOf<String, SKProduct>()

    actual suspend fun initialize() {
        if (!SKPaymentQueue.canMakePayments()) {
            throw IllegalStateException("In-app purchases are disabled")
        }
    }

    actual suspend fun getProducts(productIds: List<String>): List<StoreProduct> {
        return suspendCancellableCoroutine { continuation ->
            val productIdentifiers = NSSet.setWithArray(productIds)
            val request = SKProductsRequest(productIdentifiers)

            val delegate = object : NSObject(), SKProductsRequestDelegateProtocol {
                override fun productsRequest(request: SKProductsRequest, didReceiveResponse: SKProductsResponse) {
                    val skProducts = didReceiveResponse.products.map { it as SKProduct }
                    products = skProducts

                    skProducts.forEach { product ->
                        productMap[product.productIdentifier] = product
                    }

                    val storeProducts = skProducts.map { skProduct ->
                        StoreProduct(
                            id = skProduct.productIdentifier,
                            title = skProduct.localizedTitle,
                            description = skProduct.localizedDescription,
                            price = formatPrice(skProduct),
                            priceAmountMicros = (skProduct.price.doubleValue * 1_000_000).toLong()
                        )
                    }

                    continuation.resume(storeProducts)
                }

                override fun request(request: SKRequest, didFailWithError: NSError) {
                    continuation.resume(emptyList())
                }
            }

            request.delegate = delegate
            request.start()
        }
    }

    actual suspend fun purchase(productId: String): PurchaseResult {
        val product = productMap[productId]
            ?: return PurchaseResult.Error("Product not found. Call getProducts() first")

        return suspendCancellableCoroutine { continuation ->
            val payment = SKPayment.paymentWithProduct(product)

            val observer = object : NSObject(), SKPaymentTransactionObserverProtocol {
                override fun paymentQueue(queue: SKPaymentQueue, updatedTransactions: List<*>) {
                    updatedTransactions.forEach { transaction ->
                        val skTransaction = transaction as SKPaymentTransaction

                        when (skTransaction.transactionState) {
                            SKPaymentTransactionState.SKPaymentTransactionStatePurchased -> {
                                SKPaymentQueue.defaultQueue().finishTransaction(skTransaction)
                                SKPaymentQueue.defaultQueue().removeTransactionObserver(this)
                                continuation.resume(PurchaseResult.Success)
                            }
                            SKPaymentTransactionState.SKPaymentTransactionStateFailed -> {
                                SKPaymentQueue.defaultQueue().finishTransaction(skTransaction)
                                SKPaymentQueue.defaultQueue().removeTransactionObserver(this)

                            }
                        SKPaymentTransactionState.SKPaymentTransactionStateRestored -> {
                                SKPaymentQueue.defaultQueue().finishTransaction(skTransaction)
                                SKPaymentQueue.defaultQueue().removeTransactionObserver(this)
                                continuation.resume(PurchaseResult.AlreadyPurchased)
                            }
                            else -> {

                            }
                        }
                    }
                }
            }

            SKPaymentQueue.defaultQueue().addTransactionObserver(observer)
            SKPaymentQueue.defaultQueue().addPayment(payment)
        }
    }

    actual suspend fun restorePurchases(): RestoreResult {
        return suspendCancellableCoroutine { continuation ->
            val observer = object : NSObject(), SKPaymentTransactionObserverProtocol {
                private val restoredProductIds = mutableListOf<String>()

                override fun paymentQueue(queue: SKPaymentQueue, updatedTransactions: List<*>) {
                    updatedTransactions.forEach { transaction ->
                        val skTransaction = transaction as SKPaymentTransaction

                        when (skTransaction.transactionState) {
                            SKPaymentTransactionState.SKPaymentTransactionStateRestored -> {
                                restoredProductIds.add(skTransaction.payment.productIdentifier)
                                SKPaymentQueue.defaultQueue().finishTransaction(skTransaction)
                            }
                            SKPaymentTransactionState.SKPaymentTransactionStateFailed -> {
                                SKPaymentQueue.defaultQueue().finishTransaction(skTransaction)
                            }
                            else -> {

                            }
                        }
                    }
                }

                override fun paymentQueueRestoreCompletedTransactionsFinished(queue: SKPaymentQueue) {
                    SKPaymentQueue.defaultQueue().removeTransactionObserver(this)
                    continuation.resume(RestoreResult.Success(restoredProductIds))
                }

                override fun paymentQueue(queue: SKPaymentQueue, restoreCompletedTransactionsFailedWithError: NSError) {
                    SKPaymentQueue.defaultQueue().removeTransactionObserver(this)
                    continuation.resume(RestoreResult.Error(restoreCompletedTransactionsFailedWithError.localizedDescription))
                }
            }

            SKPaymentQueue.defaultQueue().addTransactionObserver(observer)
            SKPaymentQueue.defaultQueue().restoreCompletedTransactions()
        }
    }

    private fun formatPrice(product: SKProduct): String {
        val formatter = NSNumberFormatter()
        formatter.numberStyle = NSNumberFormatterCurrencyStyle
        formatter.locale = product.priceLocale
        return formatter.stringFromNumber(product.price) ?: "$0.99"
    }
}