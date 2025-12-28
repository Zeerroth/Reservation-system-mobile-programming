package com.example.rezervasyon.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Session manager for handling user login sessions
 * Uses SharedPreferences to store user session data
 */
class SessionManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "rezervasyon_session"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_IS_ADMIN = "is_admin"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
    
    /**
     * Save user session after successful login
     */
    fun saveUserSession(userId: Long, email: String, name: String, isAdmin: Boolean) {
        prefs.edit().apply {
            putLong(KEY_USER_ID, userId)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_NAME, name)
            putBoolean(KEY_IS_ADMIN, isAdmin)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }
    
    /**
     * Clear user session (logout)
     */
    fun clearSession() {
        prefs.edit().clear().apply()
    }
    
    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * Get current user ID
     */
    fun getUserId(): Long {
        return prefs.getLong(KEY_USER_ID, -1)
    }
    
    /**
     * Get current user email
     */
    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }
    
    /**
     * Get current user name
     */
    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }
    
    /**
     * Check if current user is admin
     */
    fun isAdmin(): Boolean {
        return prefs.getBoolean(KEY_IS_ADMIN, false)
    }
}
