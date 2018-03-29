package me.wcy.androidweekly.storage.sp

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object AppPreference {
    private var sp: SharedPreferences? = null

    fun init(context: Context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun isAutoRefresh(): Boolean {
        return sp!!.getBoolean("key_auto_refresh", true)
    }
}