package com.chickiroad.app.di

import com.chickiroad.app.game.presentation.GameViewModel
import com.chickiroad.app.managers.AppSettings
import com.chickiroad.app.managers.AudioManager
import com.chickiroad.app.purchases.StoreManager
import com.chickiroad.app.purchases.StoreRepository
import com.chickiroad.app.shop.presentation.ShopViewModel
import com.chickiroad.app.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::AppSettings)
    singleOf(::AudioManager)
    singleOf(::StoreManager)
    singleOf(::StoreRepository)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::GameViewModel)
    viewModelOf(::ShopViewModel)
}