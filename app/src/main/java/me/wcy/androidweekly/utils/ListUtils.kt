package me.wcy.androidweekly.utils

/**
 * Created by hzwangchenyan on 2017/12/11.
 */
object ListUtils {

    fun <T> isEmpty(list: Collection<T>?): Boolean {
        return list == null || list.isEmpty()
    }

    fun <T> isEmpty(array: Array<T>?): Boolean {
        return array == null || array.isEmpty()
    }
}
