package com.istighfar.counter.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "counter_prefs")

class CounterRepository(private val context: Context) {

    companion object {
        private val TODAY_COUNT = intPreferencesKey("today_count")
        private val LIFETIME_COUNT = intPreferencesKey("lifetime_count")
        private val LAST_DATE = stringPreferencesKey("last_date")
        private val AUTO_RESET_ENABLED = booleanPreferencesKey("auto_reset_enabled")
        private val SELECTED_DHIKR = stringPreferencesKey("selected_dhikr")

        val DHIKR_LIST = listOf(
            "أستغفر الله",
            "سبحان الله",
            "الحمد لله",
            "الله أكبر",
            "لا إله إلا الله",
            "لا حول ولا قوة إلا بالله",
            "سبحان الله وبحمده",
            "سبحان الله العظيم"
        )
    }

    val todayCountFlow: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[TODAY_COUNT] ?: 0
    }

    val lifetimeCountFlow: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[LIFETIME_COUNT] ?: 0
    }

    val autoResetEnabledFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[AUTO_RESET_ENABLED] ?: true
    }

    val selectedDhikrFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[SELECTED_DHIKR] ?: DHIKR_LIST[0]
    }

    suspend fun incrementCount() {
        val today = LocalDate.now().toString()
        context.dataStore.edit { prefs ->
            val lastDate = prefs[LAST_DATE] ?: ""
            if (lastDate != today) {
                prefs[TODAY_COUNT] = 0
                prefs[LAST_DATE] = today
            }
            val current = prefs[TODAY_COUNT] ?: 0
            prefs[TODAY_COUNT] = current + 1
            val lifetime = prefs[LIFETIME_COUNT] ?: 0
            prefs[LIFETIME_COUNT] = lifetime + 1
        }
    }

    suspend fun resetTodayCount() {
        context.dataStore.edit { prefs ->
            prefs[TODAY_COUNT] = 0
            prefs[LAST_DATE] = LocalDate.now().toString()
        }
    }

    suspend fun setAutoReset(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[AUTO_RESET_ENABLED] = enabled
        }
    }

    suspend fun setSelectedDhikr(dhikr: String) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_DHIKR] = dhikr
        }
    }

    suspend fun getSnapshot(): Triple<Int, Int, String> {
        val prefs = context.dataStore.data.first()
        val today = LocalDate.now().toString()
        val lastDate = prefs[LAST_DATE] ?: ""
        val todayCount = if (lastDate == today) prefs[TODAY_COUNT] ?: 0 else 0
        val lifetimeCount = prefs[LIFETIME_COUNT] ?: 0
        val dhikr = prefs[SELECTED_DHIKR] ?: DHIKR_LIST[0]
        return Triple(todayCount, lifetimeCount, dhikr)
    }

    suspend fun isAutoResetEnabled(): Boolean {
        return context.dataStore.data.first()[AUTO_RESET_ENABLED] ?: true
    }
}
