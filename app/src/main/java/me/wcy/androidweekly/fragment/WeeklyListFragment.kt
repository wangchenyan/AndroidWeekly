package me.wcy.androidweekly.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView
import kotlinx.android.synthetic.main.fragment_weekly_list.view.*
import me.wcy.androidweekly.R
import me.wcy.androidweekly.activity.SearchActivity
import me.wcy.androidweekly.api.Api
import me.wcy.androidweekly.api.SafeObserver
import me.wcy.androidweekly.constants.Extras
import me.wcy.androidweekly.model.Weekly
import me.wcy.androidweekly.storage.cache.WeeklyCache
import me.wcy.androidweekly.storage.sp.AppPreference
import me.wcy.androidweekly.utils.ListUtils
import me.wcy.androidweekly.utils.ToastUtils
import me.wcy.androidweekly.viewholder.WeeklyViewHolder
import me.wcy.androidweekly.widget.radapter.RAdapter
import me.wcy.androidweekly.widget.radapter.RSingleDelegate

/**
 * Created by hzwangchenyan on 2018/3/13.
 */
class WeeklyListFragment : BaseNaviFragment(), SwipeRefreshLayout.OnRefreshListener, SwipeMenuRecyclerView.LoadMoreListener {
    private val weeklyList: MutableList<Weekly> = mutableListOf()
    private val adapter: RAdapter<Weekly> = RAdapter(weeklyList, RSingleDelegate(WeeklyViewHolder::class.java))
    private var page = 1

    private var type = TYPE_WEEKLY

    companion object {
        const val TYPE_WEEKLY = 0
        const val TYPE_SPECIAL = 1

        fun newInstance(type: Int): WeeklyListFragment {
            val bundle = Bundle()
            bundle.putInt(Extras.TYPE, type)
            val fragment = WeeklyListFragment()
            fragment.arguments = bundle
            fragment.type = type
            return fragment
        }
    }

    override fun onLazyCreate() {
        type = arguments.getInt(Extras.TYPE, TYPE_WEEKLY)

        view!!.rv_weekly.setLoadMoreListener(this)
        view!!.rv_weekly.useDefaultLoadMore()
        view!!.rv_weekly.layoutManager = LinearLayoutManager(context)
        view!!.rv_weekly.adapter = adapter

        view!!.refresh_layout.setOnRefreshListener(this)

        if (type == TYPE_WEEKLY) {
            val history = WeeklyCache.get().get(context)
            if (!ListUtils.isEmpty(history)) {
                weeklyList.addAll(history!!)
                adapter.notifyDataSetChanged()
                view!!.rv_weekly.loadMoreFinish(false, true)
            }
            if (ListUtils.isEmpty(history) || AppPreference.isAutoRefresh()) {
                getWeekly(page)
                view!!.rv_weekly.post { view!!.refresh_layout.isRefreshing = true }
            }
        } else {
            getWeekly(page)
            view!!.rv_weekly.post { view!!.refresh_layout.isRefreshing = true }
        }
    }

    override fun layoutResId(): Int {
        return R.layout.fragment_weekly_list
    }

    override fun navigationMenuId(): Int {
        return if (type == TYPE_WEEKLY) R.id.action_weekly else R.id.action_special
    }

    override fun getMenuItemIds(): Array<Int> {
        return arrayOf(R.id.action_search)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_search -> {
                SearchActivity.start(context)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        page = 1
        getWeekly(page)
    }

    override fun onLoadMore() {
        page++
        getWeekly(page)
    }

    private fun getWeekly(page: Int) {
        val single = if (type == TYPE_WEEKLY) Api.get().getWeeklyList(page) else Api.get().getSpecialWeeklyList(page)
        single.subscribe(object : SafeObserver<List<Weekly>>(this) {
            override fun onResult(t: List<Weekly>?, e: Throwable?) {
                view!!.refresh_layout.post {
                    view!!.refresh_layout.isRefreshing = false
                }
                if (e == null) {
                    if (!ListUtils.isEmpty(t)) {
                        view!!.rv_weekly.loadMoreFinish(false, t!!.size >= Api.PAGE_SIZE)
                        if (page == 1) {
                            weeklyList.clear()
                            if (type == TYPE_WEEKLY) {
                                WeeklyCache.get().save(context, t)
                            }
                        }
                        weeklyList.addAll(t)
                        adapter.notifyDataSetChanged()
                    } else {
                        view!!.rv_weekly.loadMoreFinish(false, false)
                    }
                } else {
                    if (page == 1) {
                        ToastUtils.show("加载失败，请下拉刷新")
                    } else {
                        view!!.rv_weekly.loadMoreError(0, null)
                    }
                }
            }
        })
    }
}