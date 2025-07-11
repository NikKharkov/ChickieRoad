package com.chickiroad.app

import android.app.Application
import com.chickiroad.app.di.initKoin
import org.koin.android.ext.koin.androidContext

class ChickenApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@ChickenApplication)
        }
    }
}