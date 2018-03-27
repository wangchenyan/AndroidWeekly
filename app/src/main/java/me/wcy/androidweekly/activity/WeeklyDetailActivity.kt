package me.wcy.androidweekly.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.view.menu.MenuBuilder
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.TextView
import com.hwangjr.rxbus.RxBus
import me.wcy.androidweekly.R
import me.wcy.androidweekly.api.Api
import me.wcy.androidweekly.api.SafeObserver
import me.wcy.androidweekly.constants.Extras
import me.wcy.androidweekly.constants.RxBusTags
import me.wcy.androidweekly.model.Weekly
import me.wcy.androidweekly.model.WeeklyDetail
import me.wcy.androidweekly.storage.db.DBManager
import me.wcy.androidweekly.storage.db.greendao.WeeklyDao
import me.wcy.androidweekly.storage.sp.ReadPreference
import me.wcy.androidweekly.utils.ToastUtils
import me.wcy.androidweekly.utils.binding.Bind

class WeeklyDetailActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.refresh_layout)
    private val refreshLayout: SwipeRefreshLayout? = null
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
        refreshLayout!!.setOnRefreshListener(this)
        refreshLayout.post {
            refreshLayout.isRefreshing = true
        }
        getWeeklyDetail()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_collect, menu)
        val collectItem = menu!!.findItem(R.id.action_collect)
        val collection = DBManager.get().getWeeklyEntityDao()!!.queryBuilder().where(WeeklyDao.Properties.Url.eq(weekly!!.url)).unique()
        setCollectMenuItem(collectItem, collection != null)
        return true
    }

    @SuppressLint("RestrictedApi")
    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        return super.onMenuOpened(featureId, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_collect -> {
                val collected: Boolean
                if (!DBManager.get().hasCollect(weekly!!)) {
                    collected = true
                    weekly!!.time = System.currentTimeMillis()
                    DBManager.get().getWeeklyEntityDao()!!.insert(weekly)
                } else {
                    collected = false
                    val entity = DBManager.get()
                            .getWeeklyEntityDao()!!
                            .queryBuilder()
                            .where(WeeklyDao.Properties.Url.eq(weekly!!.url))
                            .unique()
                    if (entity != null) {
                        DBManager.get().getWeeklyEntityDao()!!.delete(entity)
                    }
                }
                RxBus.get().post(RxBusTags.WEEKLY_COLLECTION, weekly)
                setCollectMenuItem(item, collected)
                ToastUtils.show(if (collected) "已收藏" else "已取消收藏")
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

    override fun onRefresh() {
        getWeeklyDetail()
    }

    private fun getWeeklyDetail() {
        Api.get().getWeeklyDetail(weekly!!.url!!)
                .subscribe(object : SafeObserver<WeeklyDetail>(this as Activity) {
                    override fun onResult(t: WeeklyDetail?, e: Throwable?) {
                        refreshLayout!!.post {
                            refreshLayout.isRefreshing = false
                        }
                        if (e == null) {
                            refreshLayout.isEnabled = false
                            showWeeklyDetail(t!!)
                        } else {
                            ToastUtils.show("加载失败，请下拉刷新")
                        }
                    }
                })
    }

    private fun showWeeklyDetail(weeklyDetail: WeeklyDetail) {
        weeklyDetail.groupList!!.forEach { linkGroup ->
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
                linkTitle.isSelected = ReadPreference.hasRead(link.url)
                linkSummary.text = link.summary
                linkSummary.visibility = if (TextUtils.isEmpty(link.summary)) GONE else VISIBLE
                linkItem.setOnClickListener {
                    BrowserActivity.start(this, link)
                    ReadPreference.read(link.url)
                    linkTitle.isSelected = true
                }
                linkContainer.addView(linkItem)
                index++
            }
            linkGroupContainer!!.addView(group)
        }
    }

    private fun setCollectMenuItem(item: MenuItem, collected: Boolean) {
        item.title = if (collected) "已收藏" else "收藏"
        item.setIcon(if (collected) R.drawable.ic_menu_star_selected else R.drawable.ic_menu_star)
    }
}
