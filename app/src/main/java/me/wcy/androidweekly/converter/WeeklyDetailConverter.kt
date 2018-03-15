package me.wcy.androidweekly.converter

import android.text.TextUtils
import me.wcy.androidweekly.api.Api
import me.wcy.androidweekly.model.Link
import me.wcy.androidweekly.model.LinkGroup
import me.wcy.androidweekly.model.WeeklyDetail
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Created by hzwangchenyan on 2018/3/15.
 */
class WeeklyDetailConverter : Converter<WeeklyDetail> {

    override fun convert(html: String): WeeklyDetail {
        if (TextUtils.isEmpty(html)) {
            throw NullPointerException()
        }

        val weeklyDetail = WeeklyDetail()
        weeklyDetail.newsList = mutableListOf()
        val baseUrl = Api.BASE_URL.dropLast(1)
        val document = Jsoup.parse(html, baseUrl)
        val contentEle = document.getElementsByClass("post-entry")[0]
        val articleEle = contentEle.getElementsByClass("kg-card-markdown")[0]
        val h3Tags = articleEle.getElementsByTag("h3")
        val olTags = articleEle.getElementsByTag("ol")
        for (i in h3Tags.indices) {
            weeklyDetail.newsList!!.add(parseLinkGroup(h3Tags[i], olTags[i]))
        }

//        val bookEle = contentEle.getElementById("post-book-list")
//        if (bookEle != null) {
//            weeklyDetail.newsList!!.add(parseLinkGroup(bookEle))
//        }
//
//        val eventEle = contentEle.getElementById("post-event-list")
//        if (eventEle != null) {
//            weeklyDetail.newsList!!.add(parseLinkGroup(eventEle))
//        }
//
//        val jobsEle = contentEle.getElementById("post-jobs-info-list")
//        if (jobsEle != null) {
//            weeklyDetail.newsList!!.add(parseLinkGroup(jobsEle))
//        }

        return weeklyDetail
    }

    private fun parseLinkGroup(div: Element): LinkGroup {
        val h3 = div.getElementsByTag("h3")[0]
        val ol = div.getElementsByTag("ol")[0]
        return parseLinkGroup(h3, ol)
    }

    private fun parseLinkGroup(h3: Element, ol: Element): LinkGroup {
        val linkGroup = LinkGroup()
        linkGroup.title = h3.text()
        linkGroup.links = mutableListOf()
        val liTags = ol.getElementsByTag("li")
        for (liTag in liTags) {
            linkGroup.links!!.add(parseLink(liTag))
        }
        return linkGroup
    }

    private fun parseLink(li: Element): Link {
        val link = Link()
        val aTag = li.getElementsByTag("a")[0]
        link.title = aTag.text()
        link.url = aTag.absUrl("href")
        val pTags = li.getElementsByTag("p")
        if (pTags.size >= 2) {
            link.summary = pTags[1].text()
        }
        return link
    }
}