package me.wcy.androidweekly.fragment

import android.support.v7.widget.LinearLayoutManager
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import kotlinx.android.synthetic.main.fragment_collect_weekly.*
import me.wcy.androidweekly.R
import me.wcy.androidweekly.constants.RxBusTags
import me.wcy.androidweekly.model.Weekly
import me.wcy.androidweekly.storage.db.DBManager
import me.wcy.androidweekly.storage.db.greendao.WeeklyDao
import me.wcy.androidweekly.viewholder.WeeklyViewHolder
import me.wcy.androidweekly.widget.radapter.RAdapter
import me.wcy.androidweekly.widget.radapter.RSingleDelegate

/**
 * Created by hzwangchenyan on 2018/3/23.
 */
class CollectWeeklyFragment : BaseLazyFragment() {
    private val weeklyList = mutableListOf<Weekly>()
    private val adapter: RAdapter<Weekly> = RAdapter(weeklyList, RSingleDelegate(WeeklyViewHolder::class.java))

    override fun layoutResId(): Int {
        return R.layout.fragment_collect_weekly
    }

    override fun onLazyCreate() {
        rv_collect_weekly.layoutManager = LinearLayoutManager(context)
        rv_collect_weekly.adapter = adapter
        showList()
    }

    private fun showList() {
        val list = DBManager.get().getWeeklyEntityDao()!!.queryBuilder().orderDesc(WeeklyDao.Properties.Time).list()
        weeklyList.clear()
        weeklyList.addAll(list)
        adapter.notifyDataSetChanged()
    }

    @Suppress("unused")
    @Subscribe(tags = [(Tag(RxBusTags.WEEKLY_COLLECTION))])
    fun onCollect(weekly: Weekly) {
        showList()
    }
}