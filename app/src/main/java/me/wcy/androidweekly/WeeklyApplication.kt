package me.wcy.androidweekly

import android.app.Application
import me.wcy.androidweekly.storage.ReadPreference
import me.wcy.androidweekly.storage.db.DBManager
import me.wcy.androidweekly.utils.ToastUtils

/**
 * Created by hzwangchenyan on 2018/3/19.
 */
class WeeklyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ToastUtils.init(this)
        DBManager.get().init(this)
        ReadPreference.init(this)
    }
}