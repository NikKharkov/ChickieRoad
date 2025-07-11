package com.chickiroad.app.purchases.model

sealed class RestoreResult {
    data class Success(val restoredProductIds: List<String>) : RestoreResult()
    data class Error(val message: String) : RestoreResult()
}