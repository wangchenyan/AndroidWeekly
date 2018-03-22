package me.wcy.androidweekly.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.TextView
import me.wcy.androidweekly.R
import me.wcy.androidweekly.api.Api
import me.wcy.androidweekly.api.SafeObserver
import me.wcy.androidweekly.constants.Extras
import me.wcy.androidweekly.model.DTO
import me.wcy.androidweekly.model.Weekly
import me.wcy.androidweekly.model.WeeklyDetail
import me.wcy.androidweekly.storage.db.DBManager
import me.wcy.androidweekly.storage.db.greendao.WeeklyEntityDao
import me.wcy.androidweekly.utils.ToastUtils
import me.wcy.androidweekly.utils.binding.Bind

class WeeklyDetailActivity : BaseActivity() {
    @Bind(R.id.link_group_container)
    private val linkGroupContainer: LinearLayout? = null

    private var weekly: Weekly? = null

    companion object {
        fun start(context: Context, weekly: Weekly) {
            val intent = Intent(context, WeeklyDetailActivity::class.java)
            intent.putExtra(Extras.WEEKLY, weekly)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekly_detail)

        weekly = intent.getSerializableExtra(Extras.WEEKLY) as Weekly?

        title = weekly!!.title
        getWeeklyDetail(weekly!!.url!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_collect, menu)
        val collectItem = menu!!.findItem(R.id.action_collect)
        val collection = DBManager.get().getWeeklyEntityDao()!!.queryBuilder().where(WeeklyEntityDao.Properties.Url.eq(weekly!!.url)).unique()
        collectItem.title = if (collection == null) "收藏" else "已收藏"
        collectItem.setIcon(if (collection == null) R.drawable.ic_menu_star else R.drawable.ic_menu_star_selected)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_collect -> {
                if (item.title == "收藏") {
                    DBManager.get().getWeeklyEntityDao()!!.insert(DTO.toWeeklyEntity(weekly!!))
                    item.title = "已收藏"
                    ToastUtils.show("已收藏")
                } else {
                    val entity = DBManager.get().getWeeklyEntityDao()!!.queryBuilder().where(WeeklyEntityDao.Properties.Url.eq(weekly!!.url)).build().unique()
                    if (entity != null) {
                        DBManager.get().getWeeklyEntityDao()!!.delete(entity)
                    }
                    item.title = "收藏"
                    ToastUtils.show("已取消收藏")
                }
                return true
            }
            R.id.action_open_in_browser -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(weekly!!.url))
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                    ToastUtils.show("打开失败")
                }
                return true
            }
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, weekly!!.title.plus("\n").plus(weekly!!.url))
                startActivity(Intent.createChooser(intent, "分享"))
            }
        }
        return super.onOptionsItemSelected(item)
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
        weeklyDetail.newsList!!.forEach { linkGroup ->
            val group = LayoutInflater.from(this).inflate(R.layout.link_group, linkGroupContainer, false)
            val linkContainer = group.findViewById<LinearLayout>(R.id.link_container)
            val groupTitle = group.findViewById<TextView>(R.id.tv_group_title)
            groupTitle.text = linkGroup.title
            var index = 1
            linkGroup.links!!.forEach { link ->
                val linkItem = LayoutInflater.from(this).inflate(R.layout.link_item, linkContainer, false)
                val linkTitle = linkItem.findViewById<TextView>(R.id.tv_title)
                val linkSummary = linkItem.findViewById<TextView>(R.id.tv_summary)
                linkTitle.text = index.toString().plus(". ").plus(link.title)
                linkSummary.text = link.summary
                linkSummary.visibility = if (TextUtils.isEmpty(link.summary)) GONE else VISIBLE
                linkItem.setOnClickListener {
                    BrowserActivity.start(this, link)
                }
                linkContainer.addView(linkItem)
                index++
            }
            linkGroupContainer!!.addView(group)
        }
    }
}
