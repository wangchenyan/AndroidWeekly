package me.wcy.androidweekly.storage

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParser
import me.wcy.androidweekly.model.Weekly
import java.io.File
import java.util.*

/**
 * Created by hzwangchenyan on 2018/3/22.
 */
class WeeklyCache private constructor() {
    companion object {
        private const val FILE_NAME = "weekly_cache"
        fun get() = SingletonHolder.instance
    }

    private object SingletonHolder {
        val instance = WeeklyCache()
    }

    fun get(context: Context): List<Weekly>? {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.isFile) {
            file.delete()
        }
        if (file.exists()) {
            try {
                val jArray = JsonParser().parse(file.readText()).asJsonArray
                val list = ArrayList<Weekly>(jArray.size())
                for (obj in jArray) {
                    val cityEntity = Gson().fromJson<Weekly>(obj, Weekly::class.java)
                    list.add(cityEntity)
                }
                return list
            } catch (e: Exception) {
            }
        }
        return null
    }

    fun save(context: Context, list: List<Weekly>) {
        val json = Gson().toJson(list)
        val file = File(context.filesDir, FILE_NAME)
        file.writeText(json)
    }
}