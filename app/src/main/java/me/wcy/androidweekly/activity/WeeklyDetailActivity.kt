package me.wcy.androidweekly.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.TextView
import me.wcy.androidweekly.R
import me.wcy.androidweekly.api.Api
import me.wcy.androidweekly.api.SafeObserver
import me.wcy.androidweekly.constants.Extras
import me.wcy.androidweekly.model.WeeklyDetail
import me.wcy.androidweekly.utils.binding.Bind

class WeeklyDetailActivity : BaseActivity() {
    @Bind(R.id.link_group_container)
    private val linkGroupContainer: LinearLayout? = null

    companion object {
        fun start(context: Context, title: String, url: String) {
            val intent = Intent(context, WeeklyDetailActivity::class.java)
            intent.putExtra(Extras.TITLE, title)
            intent.putExtra(Extras.URL, url)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekly_detail)

        val title = intent.getStringExtra(Extras.TITLE)
        val url = intent.getStringExtra(Extras.URL)

        setTitle(title)
        getWeeklyDetail(url)
    }

    private fun getWeeklyDetail(url: String) {
        Api.get().getWeeklyDetail(url)
                .subscribe(object : SafeObserver<WeeklyDetail>(this as Activity) {
                    override fun onResult(t: WeeklyDetail?, e: Throwable?) {
                        if (e == null) {
                            showWeeklyDetail(t!!)
                        }
                    }
                })
    }

    private fun showWeeklyDetail(weeklyDetail: WeeklyDetail) {
        weeklyDetail.newsList!!.forEach {
            val group = LayoutInflater.from(this).inflate(R.layout.link_group, linkGroupContainer, false)
            val linkContainer = group.findViewById<LinearLayout>(R.id.link_container)
            val groupTitle = group.findViewById<TextView>(R.id.tv_group_title)
            groupTitle.text = it.title
            it.links!!.forEach {
                val linkItem = LayoutInflater.from(this).inflate(R.layout.link_item, linkContainer, false)
                val linkTitle = linkItem.findViewById<TextView>(R.id.tv_title)
                val linkSummary = linkItem.findViewById<TextView>(R.id.tv_summary)
                linkTitle.text = it.title
                linkSummary.text = it.summary
                linkSummary.visibility = if (TextUtils.isEmpty(it.summary)) GONE else VISIBLE
                linkItem.setOnClickListener {
                    // TODO 打开链接
                }
                linkContainer.addView(linkItem)
            }
            linkGroupContainer!!.addView(group)
        }
    }
}
