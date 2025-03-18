package com.example.medilinkapp.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore extension
private val Context.dataStore by preferencesDataStore(name = "favorites_prefs")

class FavoritesDataStore(private val context: Context) {
    companion object {
        private val FAVORITES_KEY = stringSetPreferencesKey("favorite_pharmacies")
    }

    // Read favorites from DataStore
    val favoritePharmacies: Flow<Set<String>> = context.dataStore.data
        .map { preferences -> preferences[FAVORITES_KEY] ?: emptySet() }

    // Save favorite pharmacy
    suspend fun saveFavorite(pharmacyName: String) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITES_KEY] ?: emptySet()
            preferences[FAVORITES_KEY] = currentFavorites + pharmacyName
        }
    }

    // Remove favorite pharmacy
    suspend fun removeFavorite(pharmacyName: String) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITES_KEY] ?: emptySet()
            preferences[FAVORITES_KEY] = currentFavorites - pharmacyName
        }
    }
}
