package me.wcy.androidweekly.fragment

import android.support.v4.widget.SwipeRefreshLayout
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_jobs.*
import me.wcy.androidweekly.R
import me.wcy.androidweekly.activity.BrowserActivity
import me.wcy.androidweekly.api.Api
import me.wcy.androidweekly.api.SafeObserver
import me.wcy.androidweekly.model.Jobs
import me.wcy.androidweekly.storage.sp.ReadPreference
import me.wcy.androidweekly.utils.ToastUtils

/**
 * Created by hzwangchenyan on 2018/3/26.
 */
class JobsFragment : BaseNaviFragment(), SwipeRefreshLayout.OnRefreshListener {
    private var jobs: Jobs? = null

    override fun layoutResId(): Int {
        return R.layout.fragment_jobs
    }

    override fun navigationMenuId(): Int {
        return R.id.action_jobs
    }

    override fun getMenuItemIds(): Array<Int> {
        return arrayOf(R.id.action_publish)
    }

    override fun onLazyCreate() {
        refresh_layout.setOnRefreshListener(this)
        refresh_layout.post { refresh_layout.isRefreshing = true }
        getJobs()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_publish -> {
                val url = jobs!!.publishUrl
                BrowserActivity.start(context, url!!)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        getJobs()
    }

    private fun getJobs() {
        Api.get().getJobs()
                .subscribe(object : SafeObserver<Jobs>(this) {
                    override fun onResult(t: Jobs?, e: Throwable?) {
                        refresh_layout.post { refresh_layout.isRefreshing = false }
                        if (e == null) {
                            refresh_layout.isEnabled = false
                            jobs = t
                            showJobs()
                        } else {
                            ToastUtils.show("加载失败，请下拉刷新")
                        }
                    }
                })
    }

    private fun showJobs() {
        jobs!!.groupList!!.forEach { linkGroup ->
            val group = LayoutInflater.from(context).inflate(R.layout.link_group, link_group_container, false)
            val linkContainer = group.findViewById<LinearLayout>(R.id.link_container)
            val groupTitle = group.findViewById<TextView>(R.id.tv_group_title)
            groupTitle.text = linkGroup.title
            var index = 1
            linkGroup.links!!.forEach { link ->
                val linkItem = LayoutInflater.from(context).inflate(R.layout.link_item, linkContainer, false)
                val linkTitle = linkItem.findViewById<TextView>(R.id.tv_title)
                val linkSummary = linkItem.findViewById<TextView>(R.id.tv_summary)
                linkTitle.text = index.toString().plus(". ").plus(link.title)
                linkTitle.isSelected = TextUtils.isEmpty(link.url) || ReadPreference.hasRead(link.url)
                linkSummary.text = link.summary
                linkSummary.visibility = if (TextUtils.isEmpty(link.summary)) View.GONE else View.VISIBLE
                if (!TextUtils.isEmpty(link.url)) {
                    linkItem.setOnClickListener {
                        BrowserActivity.start(context, link.url)
                        ReadPreference.read(link.url)
                        linkTitle.isSelected = true
                    }
                }
                linkContainer.addView(linkItem)
                index++
            }
            link_group_container.addView(group)
        }

        tv_quote.text = jobs!!.quote
    }
}