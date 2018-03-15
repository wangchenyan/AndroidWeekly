package me.wcy.androidweekly.model

/**
 * Created by hzwangchenyan on 2018/3/14.
 */
data class Weekly(
        var url: String? = null,
        var img: String? = null,
        var title: String? = null,
        var date: String? = null,
        var comment: String? = null,
        var tagList: MutableList<String>? = null,
        var author_name: String? = null,
        var author_avatar: String? = null
)

data class Link(
        var title: String? = null,
        var summary: String? = null,
        var url: String? = null
)

data class LinkGroup(
        var title: String? = null,
        var links: MutableList<Link>? = null
)

data class WeeklyDetail(
        var newsList: MutableList<LinkGroup>? = null
)