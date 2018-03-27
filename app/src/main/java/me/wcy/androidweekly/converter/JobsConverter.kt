package me.wcy.androidweekly.converter

import android.text.TextUtils
import me.wcy.androidweekly.api.Api
import me.wcy.androidweekly.model.Jobs
import me.wcy.androidweekly.model.Link
import me.wcy.androidweekly.model.LinkGroup
import org.jsoup.Jsoup

/**
 * Created by hzwangchenyan on 2018/3/26.
 */
class JobsConverter : Converter<Jobs> {
    override fun convert(html: String): Jobs {
        if (TextUtils.isEmpty(html)) {
            throw NullPointerException()
        }

        val jobs = Jobs()
        val baseUrl = Api.BASE_URL.dropLast(1)
        val document = Jsoup.parse(html, baseUrl)
        jobs.publishUrl = document.getElementsByClass("btn-default")[0].attr("href")
        jobs.quote = document.getElementsByTag("blockquote")[0].text()
        jobs.groupList = mutableListOf()
        val h3Tags = document.getElementsByTag("h3")
        val olTags = document.getElementsByTag("ol")
        for (i in h3Tags.indices) {
            val group = LinkGroup()
            group.title = h3Tags[i].text()
            group.links = mutableListOf()
            val liTags = olTags[i].getElementsByTag("li")
            for (ele in liTags) {
                val link = Link()
                val pTags = ele.getElementsByTag("p")
                link.title = pTags[0].text()
                link.summary = pTags[1].text()
                val aTags = pTags[0].getElementsByTag("a")
                if (aTags.size > 0) {
                    link.url = aTags[0].attr("href")
                }
                group.links!!.add(link)
            }
            jobs.groupList!!.add(group)
        }

        return jobs
    }
}