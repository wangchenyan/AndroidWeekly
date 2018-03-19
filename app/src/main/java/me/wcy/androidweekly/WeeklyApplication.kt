package me.wcy.androidweekly

import android.app.Application
import me.wcy.androidweekly.storage.db.DBManager

/**
 * Created by hzwangchenyan on 2018/3/19.
 */
class WeeklyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DBManager.get().init(this)
    }
}