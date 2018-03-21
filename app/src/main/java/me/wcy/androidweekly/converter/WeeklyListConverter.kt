package me.wcy.androidweekly.converter

import android.text.TextUtils
import android.util.Log
import me.wcy.androidweekly.api.Api
import me.wcy.androidweekly.model.Weekly
import org.jsoup.Jsoup
import java.lang.Exception

/**
 * Created by hzwangchenyan on 2018/3/14.
 */
class WeeklyListConverter : Converter<List<Weekly>> {

    @Throws(Exception::class)
    override fun convert(html: String): List<Weekly> {
        if (TextUtils.isEmpty(html)) {
            throw NullPointerException()
        }

        val list: MutableList<Weekly> = mutableListOf()
        val baseUrl = Api.BASE_URL.dropLast(1)
        val document = Jsoup.parse(html, baseUrl)
        val articles = document.getElementsByTag("article")
        articles.forEach {
            val weekly = Weekly()
            val header = it.getElementsByClass("card-header ")[0]
            val image = header.getElementsByClass("featured-image-container")[0]
            var imageUrl = image.attr("style")
            imageUrl = imageUrl.substring(imageUrl.indexOf("(") + 1, imageUrl.indexOf(")"))
            if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                imageUrl = "$baseUrl$imageUrl"
            }
            weekly.img = imageUrl

            val author = header.getElementsByClass("author")[0]
            weekly.author_name = author.getElementsByClass("name")[0].text()
            var authorAvatar = author.getElementsByClass("avatar")[0].attr("src")
            if (!TextUtils.isEmpty(authorAvatar) && authorAvatar.startsWith("//")) {
                authorAvatar = "http:$authorAvatar"
            }
            weekly.author_avatar = authorAvatar

            val content = it.getElementsByClass("content")[0]
            val title = content.getElementsByClass("h4 title")[0].getElementsByTag("a")[0]
            weekly.url = title.absUrl("href")
            weekly.title = title.text()
            weekly.date = content.getElementsByClass("date")[0].ownText()
            weekly.comment = content.getElementsByClass("Comment")[0].getElementsByTag("a")[0].text()
            val tagElements = content.getElementsByClass("tag-list")[0].getElementsByTag("a")
            var tags = ""
            tagElements.forEach {
                tags = tags.plus(it.text()).plus(",")
            }
            if (tags.isNotEmpty()) {
                tags = tags.dropLast(1)
            }
            weekly.tags = tags

            list.add(weekly)
        }

        Log.e("weekly", list.toString())

        return list
    }
}