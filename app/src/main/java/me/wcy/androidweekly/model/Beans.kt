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
        var groupList: MutableList<LinkGroup>? = null
) : Serializable

data class Jobs(
        var publishUrl: String? = null,
        var quote: String? = null,
        var groupList: MutableList<LinkGroup>? = null
) : Serializable