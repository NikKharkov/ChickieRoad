package com.chickiroad.app.shop.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chickieroad.composeapp.generated.resources.Res
import chickieroad.composeapp.generated.resources.bonus_doublerx2
import chickieroad.composeapp.generated.resources.bonus_doublerx5
import chickieroad.composeapp.generated.resources.bonus_hp
import chickieroad.composeapp.generated.resources.bonus_speed
import com.chickiroad.app.managers.AppSettings
import com.chickiroad.app.purchases.StoreRepository
import com.chickiroad.app.purchases.model.PurchaseResult
import com.chickiroad.app.purchases.model.RestoreResult
import com.chickiroad.app.purchases.model.StoreProduct
import com.chickiroad.app.shop.model.ShopBonus
import com.chickiroad.app.shop.model.ShopUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource

class ShopViewModel(private val storeRepository: StoreRepository) : ViewModel() {
    private val _shopUiState = MutableStateFlow(ShopUiState())
    val shopUiState: StateFlow<ShopUiState> = _shopUiState.asStateFlow()

    init {
        initializeStore()
    }

    private fun initializeStore() {
        viewModelScope.launch {
            _shopUiState.value = _shopUiState.value.copy(isLoading = true)

            try {
                storeRepository.initialize()
                loadBonuses()
            } catch (e: Exception) {
                _shopUiState.value = _shopUiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to initialize store: ${e.message}"
                )
            }
        }
    }

    private suspend fun loadBonuses() {
        try {
            val storeProducts = storeRepository.getStoreProducts()

            val bonuses = listOf(
                createBonus("double_x2", "Double x2", "Doubles the points",
                    Res.drawable.bonus_doublerx2, storeProducts),
                createBonus("health_boost", "Health +10", "Increases health by 10",
                    Res.drawable.bonus_hp, storeProducts),
                createBonus("speed_x3", "Speed x3", "Triples the speed",
                    Res.drawable.bonus_speed, storeProducts),
                createBonus("double_x5", "Double x5", "Increases points by 5 times",
                    Res.drawable.bonus_doublerx5, storeProducts)
            )

            _shopUiState.value = _shopUiState.value.copy(
                bonuses = bonuses,
                isLoading = false,
                errorMessage = null
            )
        } catch (e: Exception) {
            _shopUiState.value = _shopUiState.value.copy(
                isLoading = false,
                errorMessage = "Failed to load products: ${e.message}"
            )
        }
    }

    private fun createBonus(
        id: String,
        name: String,
        description: String,
        iconRes: DrawableResource,
        storeProducts: List<StoreProduct>
    ): ShopBonus {
        val storeProduct = storeProducts.find { it.id == id }
        val price = storeProduct?.price ?: "$0.99"

        return ShopBonus(
            id = id,
            name = name,
            description = description,
            price = price,
            iconRes = iconRes,
            isPurchased = storeRepository.isPurchased(id)
        )
    }

    fun purchaseBonus(bonusId: String) {
        viewModelScope.launch {
            _shopUiState.value = _shopUiState.value.copy(purchaseInProgress = bonusId)

            when (val result = storeRepository.purchaseProduct(bonusId)) {
                is PurchaseResult.Success -> {
                    loadBonuses()
                }
                is PurchaseResult.UserCancelled -> {

                }
                is PurchaseResult.AlreadyPurchased -> {
                    _shopUiState.value = _shopUiState.value.copy(
                        errorMessage = "Already purchased"
                    )
                }
                is PurchaseResult.Error -> {
                    _shopUiState.value = _shopUiState.value.copy(
                        errorMessage = result.message
                    )
                }
            }

            _shopUiState.value = _shopUiState.value.copy(purchaseInProgress = null)
        }
    }

    fun restorePurchases() {
        viewModelScope.launch {
            _shopUiState.value = _shopUiState.value.copy(isRestoring = true)

            when (val result = storeRepository.restorePurchases()) {
                is RestoreResult.Success -> {
                    loadBonuses()
                    _shopUiState.value = _shopUiState.value.copy(
                        errorMessage = "Restored ${result.restoredProductIds.size} purchases"
                    )
                }
                is RestoreResult.Error -> {
                    _shopUiState.value = _shopUiState.value.copy(
                        errorMessage = result.message
                    )
                }
            }

            _shopUiState.value = _shopUiState.value.copy(isRestoring = false)
        }
    }

    fun clearError() {
        _shopUiState.value = _shopUiState.value.copy(errorMessage = null)
    }
}