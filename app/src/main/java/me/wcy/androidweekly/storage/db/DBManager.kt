package me.wcy.androidweekly.storage.db

import android.content.Context
import me.wcy.androidweekly.storage.db.greendao.DaoMaster
import me.wcy.androidweekly.storage.db.greendao.DaoSession
import me.wcy.androidweekly.storage.db.greendao.LinkEntityDao
import me.wcy.androidweekly.storage.db.greendao.WeeklyEntityDao

/**
 * Created by hzwangchenyan on 2018/3/19.
 */
class DBManager private constructor() {
    private var daoSession: DaoSession? = null
    private var weeklyEntityDao: WeeklyEntityDao? = null
    private var linkEntityDao: LinkEntityDao? = null

    companion object {
        private const val DB_NAME = "weekly_database"
        fun get() = SingletonHolder.instance
    }

    private object SingletonHolder {
        val instance: DBManager = DBManager()
    }

    fun init(context: Context) {
        val helper = DaoMaster.DevOpenHelper(context, DB_NAME)
        val db = helper.writableDb
        daoSession = DaoMaster(db).newSession()
        weeklyEntityDao = daoSession!!.weeklyEntityDao
        linkEntityDao = daoSession!!.linkEntityDao
    }

    fun getWeeklyEntityDao() = weeklyEntityDao

    fun getLinkEntityDao() = linkEntityDao
}