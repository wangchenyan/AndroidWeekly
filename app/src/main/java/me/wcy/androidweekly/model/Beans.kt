package me.wcy.androidweekly.model

/**
 * Created by hzwangchenyan on 2018/3/14.
 */
data class Weekly(var url: String? = null, var img: String? = null, var title: String? = null, var date: String? = null, var comment: String? = null, var tagList: MutableList<String>? = null)