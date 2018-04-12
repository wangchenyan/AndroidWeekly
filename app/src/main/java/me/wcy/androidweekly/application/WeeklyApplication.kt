package me.wcy.androidweekly.application

import android.app.Application
import com.tencent.smtt.sdk.QbSdk
import me.wcy.androidweekly.storage.db.DBManager
import me.wcy.androidweekly.storage.sp.AppPreference
import me.wcy.androidweekly.storage.sp.ReadPreference
import me.wcy.androidweekly.utils.ToastUtils

/**
 * Created by hzwangchenyan on 2018/3/19.
 */
class WeeklyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ToastUtils.init(this)
        DBManager.get().init(this)
        AppPreference.init(this)
        ReadPreference.init(this)
        QbSdk.initX5Environment(this, null)
    }
}