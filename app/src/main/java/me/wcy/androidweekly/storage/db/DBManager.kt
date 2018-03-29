package me.wcy.androidweekly.storage.db

import android.content.Context
import me.wcy.androidweekly.model.Weekly
import me.wcy.androidweekly.storage.db.greendao.DaoMaster
import me.wcy.androidweekly.storage.db.greendao.DaoSession
import me.wcy.androidweekly.storage.db.greendao.LinkDao
import me.wcy.androidweekly.storage.db.greendao.WeeklyDao

/**
 * Created by hzwangchenyan on 2018/3/19.
 */
class DBManager private constructor() {
    private var daoSession: DaoSession? = null
    private var weeklyDao: WeeklyDao? = null
    private var linkDao: LinkDao? = null

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
        weeklyDao = daoSession!!.weeklyDao
        linkDao = daoSession!!.linkDao
    }

    fun getWeeklyEntityDao() = weeklyDao

    fun getLinkEntityDao() = linkDao

    fun hasCollect(weekly: Weekly): Boolean {
        val entity = weeklyDao!!
                .queryBuilder()
                .where(WeeklyDao.Properties.Url.eq(weekly.url))
                .unique()
        return entity != null
    }

    fun hasCollect(url: String): Boolean {
        val entity = linkDao!!
                .queryBuilder()
                .where(LinkDao.Properties.Url.eq(url))
                .unique()
        return entity != null
    }
}