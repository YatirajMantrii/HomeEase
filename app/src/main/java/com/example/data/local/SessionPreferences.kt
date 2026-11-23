package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "homeease_prefs")

class SessionPreferences(private val context: Context) {
    private object Keys {
        val LOGGED_IN_USER_ID = stringPreferencesKey("logged_in_user_id")
        val SELECTED_LOCALITY = stringPreferencesKey("selected_locality")
    }

    val loggedInUserId: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.LOGGED_IN_USER_ID] ?: DEFAULT_CUSTOMER_ID
    }

    val selectedLocality: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.SELECTED_LOCALITY] ?: "Pune (All Areas)"
    }

    suspend fun setLoggedInUserId(userId: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.LOGGED_IN_USER_ID] = userId
        }
    }

    suspend fun setSelectedLocality(locality: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SELECTED_LOCALITY] = locality
        }
    }

    companion object {
        const val DEFAULT_CUSTOMER_ID = "user_cust_aarav"
        const val DEFAULT_PRO_ID = "user_pro_rohan"
        const val GUEST_USER_ID = "guest"
    }
}
