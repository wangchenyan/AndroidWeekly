package me.wcy.androidweekly.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by hzwangchenyan on 2018/3/14.
 */
data class Weekly(
        @SerializedName("url") var url: String? = null,
        @SerializedName("img") var img: String? = null,
        @SerializedName("title") var title: String? = null,
        @SerializedName("date") var date: String? = null,
        @SerializedName("comment") var comment: String? = null,
        @SerializedName("tags") var tags: String? = null,
        @SerializedName("author_name") var author_name: String? = null,
        @SerializedName("author_avatar") var author_avatar: String? = null
) : Serializable

data class Link(
        var title: String? = null,
        var summary: String? = null,
        var url: String? = null
) : Serializable

data class LinkGroup(
        var title: String? = null,
        var links: MutableList<Link>? = null
) : Serializable

data class WeeklyDetail(
        var newsList: MutableList<LinkGroup>? = null
) : Serializable