package me.wcy.androidweekly.fragment

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView
import me.wcy.androidweekly.R
import me.wcy.androidweekly.api.Api
import me.wcy.androidweekly.api.SafeObserver
import me.wcy.androidweekly.model.Weekly
import me.wcy.androidweekly.utils.binding.Bind
import me.wcy.androidweekly.viewholder.WeeklyViewHolder
import me.wcy.androidweekly.widget.radapter.RAdapter
import me.wcy.androidweekly.widget.radapter.RSingleDelegate

/**
 * Created by hzwangchenyan on 2018/3/13.
 */
class WeeklyListFragment : BaseLazyFragment(), SwipeRefreshLayout.OnRefreshListener, SwipeMenuRecyclerView.LoadMoreListener {
    @Bind(R.id.refresh_layout)
    private val refreshLayout: SwipeRefreshLayout? = null
    @Bind(R.id.rv_weekly)
    private val rvWeekly: SwipeMenuRecyclerView? = null

    private val weeklyList: MutableList<Weekly> = mutableListOf()
    private val adapter: RAdapter<Weekly> = RAdapter(weeklyList, RSingleDelegate(WeeklyViewHolder::class.java))
    private var page = 1

    override fun onLazyCreate() {
        rvWeekly!!.setLoadMoreListener(this)
        rvWeekly.useDefaultLoadMore()
        rvWeekly.layoutManager = LinearLayoutManager(context)
        rvWeekly.adapter = adapter

        refreshLayout!!.setOnRefreshListener(this)
        rvWeekly.post {
            refreshLayout.isRefreshing = true
        }

        getWeekly(page)
    }

    override val layoutResId: Int
        get() = R.layout.fragment_weekly_list

    override fun onRefresh() {
        page = 1
        getWeekly(page)
    }

    override fun onLoadMore() {
        page++
        getWeekly(page)
    }

    private fun getWeekly(page: Int) {
        Api.get().getWeeklyList(page)
                .subscribe(object : SafeObserver<List<Weekly>>(this) {
                    override fun onResult(t: List<Weekly>?, e: Throwable?) {
                        refreshLayout!!.isRefreshing = false
                        if (e == null) {
                            if (!t!!.isEmpty()) {
                                rvWeekly!!.loadMoreFinish(false, true)
                                if (page == 1) {
                                    weeklyList.clear()
                                }
                                weeklyList.addAll(t)
                                adapter.notifyDataSetChanged()
                            } else {
                                rvWeekly!!.loadMoreFinish(false, false)
                            }
                        } else {
                            rvWeekly!!.loadMoreError(0, null)
                        }
                    }
                })
    }
}