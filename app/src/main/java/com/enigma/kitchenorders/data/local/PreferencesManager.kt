package com.enigma.kitchenorders.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {
    
    companion object {
        private val BUSINESS_NAME_KEY = stringPreferencesKey("business_name")
    }
    
    val businessName: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[BUSINESS_NAME_KEY] ?: "Dapur Saya"
    }
    
    suspend fun setBusinessName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[BUSINESS_NAME_KEY] = name
        }
    }
}
