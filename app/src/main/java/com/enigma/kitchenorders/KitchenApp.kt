package com.enigma.kitchenorders

import android.app.Application
import com.enigma.kitchenorders.data.local.AppDatabase
import com.enigma.kitchenorders.data.local.PreferencesManager
import com.enigma.kitchenorders.data.repository.KitchenRepositoryImpl
import com.enigma.kitchenorders.domain.repository.KitchenRepository

class KitchenApp : Application() {
    
    lateinit var database: AppDatabase
    lateinit var repository: KitchenRepository
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
        repository = KitchenRepositoryImpl(database)
        preferencesManager = PreferencesManager(applicationContext)
    }
}
