package me.wcy.androidweekly.model

import java.io.Serializable

/**
 * Created by hzwangchenyan on 2018/3/14.
 */
data class LinkGroup(
        var title: String? = null,
        var links: MutableList<Link>? = null
) : Serializable

data class WeeklyDetail(
        var newsList: MutableList<LinkGroup>? = null
) : Serializable