package com.example.face_id.core.session

import android.content.Context
import android.content.Intent
import android.util.Log

object SessionManager {
    private const val PREFS = "face_id_prefs"
    private const val KEY_UID = "user_id"
    private const val KEY_TOKEN = "auth_token"
    private const val TAG = "SessionManager"

    fun save(context: Context, userId: String?, token: String?) {
        val sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        sp.edit().apply {
            if (!userId.isNullOrBlank()) putString(KEY_UID, userId)
            if (!token.isNullOrBlank()) putString(KEY_TOKEN, token)
        }.apply()
        Log.d(TAG, "saved uid=${userId}, token=${token?.take(8)}...")
    }

    fun getUserId(context: Context, intent: Intent?): String? {

        val fromIntent = intent?.getStringExtra("user_id")
            ?: intent?.getStringExtra("userId")
            ?: intent?.getStringExtra("uid")

        if (!fromIntent.isNullOrBlank()) {

            save(context, fromIntent, null)
            return fromIntent
        }
        // Fallback: prefs
        val sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val fromPrefs = sp.getString(KEY_UID, null)
        Log.d(TAG, "getUserId: intent=$fromIntent, prefs=$fromPrefs")
        return fromPrefs
    }

    fun getToken(context: Context): String? {
        val sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return sp.getString(KEY_TOKEN, null)
    }
}
