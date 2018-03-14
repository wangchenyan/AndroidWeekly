package me.wcy.androidweekly.converter

import java.lang.Exception

/**
 * Created by hzwangchenyan on 2018/3/14.
 */
interface Converter<T> {
    @Throws(Exception::class)
    fun convert(html: String): T?
}