package com.clouditemapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    object PreferencesKeys {
        val IS_SOUND_ENABLED = booleanPreferencesKey("is_sound_enabled")
        val IS_MUSIC_ENABLED = booleanPreferencesKey("is_music_enabled")
        val LANGUAGE = stringPreferencesKey("language")
        val FONT_SCALE = floatPreferencesKey("font_scale")
        val IS_EYE_PROTECTION_ENABLED = booleanPreferencesKey("is_eye_protection_enabled")
    }

    val isSoundEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_SOUND_ENABLED] ?: true
    }

    val isMusicEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_MUSIC_ENABLED] ?: true
    }

    val language: Flow<String> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.LANGUAGE] ?: "中文"
    }

    val fontScale: Flow<Float> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.FONT_SCALE] ?: 1.0f
    }

    val isEyeProtectionEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_EYE_PROTECTION_ENABLED] ?: true
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_SOUND_ENABLED] = enabled
        }
    }

    suspend fun setMusicEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_MUSIC_ENABLED] = enabled
        }
    }

    suspend fun setLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language
        }
    }

    suspend fun setFontScale(scale: Float) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_SCALE] = scale
        }
    }

    suspend fun setEyeProtectionEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_EYE_PROTECTION_ENABLED] = enabled
        }
    }

    suspend fun resetAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
