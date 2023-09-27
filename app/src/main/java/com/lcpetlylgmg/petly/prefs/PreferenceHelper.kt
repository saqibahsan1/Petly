package com.lcpetlylgmg.petly.prefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.lcpetlylgmg.petly.adopt.match.data.Match
import com.lcpetlylgmg.petly.user.data.User

class PreferenceHelper {
    companion object {

        private val helper = PreferenceHelper()
        private lateinit var preferences: SharedPreferences

        fun getPref(context: Context): PreferenceHelper {
            preferences =
                context.getSharedPreferences(PreferenceKeys.KEY_PREF_NAME, Context.MODE_PRIVATE)
            return helper
        }
    }

    fun clearPreferences() {
        preferences.edit().clear().apply()
    }

    fun getPreferences(): PreferenceHelper {
        val helper = PreferenceHelper()
        return helper
    }


    fun isUserLogin(): Boolean {
        return preferences.getBoolean(PreferenceKeys.KEY_PREF_IS_LOGIN, false)
    }


    fun saveCurrentUser(user: User) {
        val gson = Gson()
        val json = gson.toJson(user)
        preferences.edit().putString(PreferenceKeys.KEY_PREF_USER, json).apply()

    }

    fun getCurrentUser(): User? {
        val gson = Gson()
        val json = preferences.getString(PreferenceKeys.KEY_PREF_USER, null)
        json?.let {
            return gson.fromJson(json, User::class.java)
        }
        return null
    }

    fun saveCharacteristics(match: Match) {
        val gson = Gson()
        val json = gson.toJson(match)
        preferences.edit().putString(PreferenceKeys.KEY_PREF_MATCH, json).apply()

    }

    fun getCurrentCharacteristics(): Match? {
        val gson = Gson()
        val json = preferences.getString(PreferenceKeys.KEY_PREF_MATCH, null)
        json?.let {
            return gson.fromJson(json, Match::class.java)
        }
        return null
    }

    fun saveBooleanValue(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun setUserLogin(value: Boolean) {
        preferences.edit().putBoolean(PreferenceKeys.KEY_PREF_IS_LOGIN, value).apply()
    }

    fun getBooleanValue(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }


    fun saveStringValue(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun getStringValue(key: String, defaultValue: String? = null): String? {
        return preferences.getString(key, defaultValue)
    }


}