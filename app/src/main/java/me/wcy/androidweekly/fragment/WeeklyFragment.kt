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
class WeeklyFragment : BaseLazyFragment() {
    @Bind(R.id.refresh_layout)
    private val refreshLayout: SwipeRefreshLayout? = null
    @Bind(R.id.rv_weekly)
    private val rvWeekly: SwipeMenuRecyclerView? = null

    private val weeklyList: MutableList<Weekly> = mutableListOf()
    private val adapter: RAdapter<Weekly> = RAdapter(weeklyList, RSingleDelegate(WeeklyViewHolder::class.java))

    override fun onLazyCreate() {
        rvWeekly!!.layoutManager = LinearLayoutManager(context)
        rvWeekly.adapter = adapter

        getWeekly(1)
    }

    override val layoutResId: Int
        get() = R.layout.fragment_weekly

    private fun getWeekly(page: Int) {
        Api.get().getWeeklyList(page)
                .subscribe(object : SafeObserver<List<Weekly>>(this) {
                    override fun onResult(t: List<Weekly>?) {
                        if (t != null) {
                            if (page == 1) {
                                weeklyList.clear()
                            }
                            weeklyList.addAll(t)
                            adapter.notifyDataSetChanged()
                        }
                    }
                })
    }
}