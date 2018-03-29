package me.wcy.androidweekly.fragment

import android.support.v7.widget.LinearLayoutManager
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import kotlinx.android.synthetic.main.fragment_collect_link.*
import me.wcy.androidweekly.R
import me.wcy.androidweekly.constants.RxBusTags
import me.wcy.androidweekly.model.Link
import me.wcy.androidweekly.storage.db.DBManager
import me.wcy.androidweekly.storage.db.greendao.LinkDao
import me.wcy.androidweekly.viewholder.LinkViewHolder
import me.wcy.androidweekly.widget.radapter.RAdapter
import me.wcy.androidweekly.widget.radapter.RSingleDelegate

/**
 * Created by hzwangchenyan on 2018/3/23.
 */
class CollectLinkFragment : BaseLazyFragment() {
    private val linkList = mutableListOf<Link>()
    private val adapter = RAdapter(linkList, RSingleDelegate(LinkViewHolder::class.java))

    override fun layoutResId(): Int {
        return R.layout.fragment_collect_link
    }

    override fun onLazyCreate() {
        rv_collect_link.layoutManager = LinearLayoutManager(context)
        rv_collect_link.adapter = adapter
        showList()
    }

    private fun showList() {
        val list = DBManager.get().getLinkEntityDao()!!.queryBuilder().orderDesc(LinkDao.Properties.Time).list()
        linkList.clear()
        linkList.addAll(list)
        adapter.notifyDataSetChanged()
    }

    @Suppress("unused")
    @Subscribe(tags = [(Tag(RxBusTags.LINK_COLLECTION))])
    fun onCollect(url: String) {
        showList()
    }
}