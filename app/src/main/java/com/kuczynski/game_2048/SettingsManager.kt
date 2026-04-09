package com.kuczynski.game_2048

import android.content.Context
import androidx.core.content.edit

/**
 * Stores/loads user settings from prefs file
 */
class SettingsManager(context: Context) {
    private val prefs = context.getSharedPreferences("game_2048_prefs", Context.MODE_PRIVATE)

    fun getSavedTheme(): AppTheme {
        val savedThemeName = prefs.getString("theme_key", AppTheme.SYSTEM.name)
        return try {
            AppTheme.valueOf(savedThemeName!!)
        } catch (e: Exception) {
            AppTheme.SYSTEM
        }
    }

    fun getHighScore(boardSize: Int): Int {
        return prefs.getInt("high_score_$boardSize", 0)
    }

    fun saveHighScore(boardSize: Int, score: Int) {
        prefs.edit().putInt("high_score_$boardSize", score).apply()
    }

    fun saveTheme(theme: AppTheme) {
        prefs.edit { putString("theme_key", theme.name) }
    }
}