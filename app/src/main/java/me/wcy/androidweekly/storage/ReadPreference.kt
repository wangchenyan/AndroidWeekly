package me.wcy.androidweekly.storage

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils

/**
 * Created by hzwangchenyan on 2018/3/22.
 */
object ReadPreference {
    private var sp: SharedPreferences? = null

    fun init(context: Context) {
        this.sp = context.getSharedPreferences("read_record", Context.MODE_PRIVATE)
    }

    fun read(url: String?) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        sp!!.edit().putInt(url, 1).apply()
    }

    fun hasRead(url: String?): Boolean {
        if (TextUtils.isEmpty(url)) {
            return false
        }
        return sp!!.contains(url)
    }
}